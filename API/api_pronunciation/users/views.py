from django.shortcuts import render
from rest_framework import status
from rest_framework.authentication import BaseAuthentication
from rest_framework.authtoken.serializers import AuthTokenSerializer
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.generics import CreateAPIView
from rest_framework.permissions import AllowAny

from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework.authtoken.models import Token

from users.models import User
from users.serializers import UserRegisterSerializer, UserLoginSerializer


class Register(CreateAPIView):
    queryset = User.objects.all()
    permission_classes = (AllowAny,)
    serializer_class = UserRegisterSerializer


class Login(ObtainAuthToken):
    serializer_class = UserLoginSerializer
