from django.utils.translation import gettext_lazy as _

from django.contrib.auth import authenticate
from rest_framework import serializers

from users.models import User


class UserRegisterSerializer(serializers.ModelSerializer):
    password = serializers.CharField(write_only=True, min_length=8, max_length=128)

    class Meta:
        model = User
        fields = ('username', 'first_name', 'last_name', 'email', 'password')


class UserLoginSerializer(serializers.Serializer):
    email_or_username = serializers.CharField(
        label=_("Username"),
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
            user = authenticate(request=self.context.get('request'),
                                email=email_or_username, password=password)
            if not user:
                msg = _('Unable to log in with provided credentials.')
                raise serializers.ValidationError(msg, code='authorization')

        attrs['user'] = user
        return attrs
