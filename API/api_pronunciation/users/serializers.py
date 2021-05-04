from django.contrib.auth.password_validation import validate_password
from django.utils.translation import gettext_lazy as _

from django.contrib.auth import authenticate
from rest_framework import serializers
from rest_framework.validators import UniqueValidator

from users.models import User


class UserRegisterSerializer(serializers.ModelSerializer):
    email = serializers.EmailField(
        required=True,
        validators=[UniqueValidator(queryset=User.objects.all())]
    )

    password = serializers.CharField(write_only=True, required=True, validators=[validate_password])

    class Meta:
        model = User
        fields = ('username', 'password', 'email', 'first_name', 'last_name')
        extra_kwargs = {
            'username': {'required': True},
        }

    def create(self, validated_data):
        user = User.objects.create(
            username=validated_data['username'],
            email=validated_data['email'],
        )
        if 'first_name' in validated_data.keys():
            user.first_name = validated_data['first_name']
        if 'last_name' in validated_data.keys():
            user.first_name = validated_data['last_name']

        user.set_password(validated_data['password'])
        user.save()

        return user


class UserLoginSerializer(serializers.Serializer):
    email_or_username = serializers.CharField(
        label=_("Email or Username"),
        write_only=True
    )
    password = serializers.CharField(
        label=_("Password"),
        style={'input_type': 'password'},
        trim_whitespace=False,
        write_only=True
    )

    def validate(self, attrs):
        email_or_username = attrs.get('email_or_username')
        password = attrs.get('password')

        if not email_or_username:
            msg = _('Must include "username" and "email".')
            raise serializers.ValidationError(msg, code='authorization')
        if not password:
            msg = _('Must include "password"')
            raise serializers.ValidationError(msg, code='authorization')

        user = authenticate(request=self.context.get('request'),
                            username=email_or_username, password=password)

        if not user:
            user_by_email = User.objects.all().get(email=email_or_username)
            if not user_by_email:
                msg = _('Unable to log in with provided credentials.')
                raise serializers.ValidationError(msg, code='authorization')
            else:
                user = authenticate(request=self.context.get('request'),
                                    username=user_by_email.username, password=password)
                if not user:
                    msg = _('Unable to log in with provided credentials.')
                    raise serializers.ValidationError(msg, code='authorization')

        attrs['user'] = user
        return attrs
