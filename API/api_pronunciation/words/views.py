import os
import random

from django.core.files.base import ContentFile
from django.core.files.storage import default_storage
from django.http import HttpResponse
from django.shortcuts import render, redirect
from django.views.generic.base import View
from rest_framework import generics, views, status
from rest_framework.generics import GenericAPIView, RetrieveAPIView, ListAPIView
from rest_framework.pagination import PageNumberPagination
from rest_framework.parsers import FileUploadParser, MultiPartParser
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.views import APIView

from api_pronunciation.settings import MEDIA_ROOT
from words.check import check_pronunciation
from words.models import Word
from words.parsers import AudioUploadParser
from words.serializers import WordDetailSerializer
from api_pronunciation import settings


class AllWordsView(ListAPIView):
    serializer_class = WordDetailSerializer
    queryset = Word.objects.all()
    pagination_class = PageNumberPagination
    permission_classes = (IsAuthenticated,)


class SearchWordsApiView(ListAPIView):
    serializer_class = WordDetailSerializer
    permission_classes = (IsAuthenticated,)

    def get_queryset(self):
        return Word.objects.filter(word__contains=self.kwargs['search_word'])[:10]


class WordDetailView(RetrieveAPIView):
    serializer_class = WordDetailSerializer
    queryset = Word.objects.all()
    permission_classes = (IsAuthenticated,)


class RandomWordView(RetrieveAPIView):
    serializer_class = WordDetailSerializer
    queryset = Word.objects.all()
    permission_classes = (IsAuthenticated,)

    def get_object(self):
        return random.choice(self.get_queryset())


class WordAudioView(GenericAPIView):
    serializer_class = WordDetailSerializer
    queryset = Word.objects.all()
    permission_classes = (IsAuthenticated,)

    def get(self, request, *args, **kwargs):
        path = self.get_object().audio_path

        with open(path, 'rb') as f:
            response = HttpResponse()
            response.write(f.read())
            response['Content-Type'] = 'audio/mp3'
            response['Content-Length'] = os.path.getsize(path)
            return response


class CheckPronunciationView(GenericAPIView):
    serializer_class = WordDetailSerializer
    queryset = Word.objects.all()
    permission_classes = (IsAuthenticated,)
    parser_classes = (MultiPartParser,)

    def put(self, request, *args, **kwargs):
        f = request.FILES['file']
        handle_uploaded_file(f)

        score = check_pronunciation(request.user, self.get_object(), 'media/user_audio.mp3')
        response_data = {'score': score}
        return Response(status=status.HTTP_200_OK, data=response_data)


def handle_uploaded_file(f):
    with open(MEDIA_ROOT + 'user_audio.mp3', 'wb+') as destination:
        for chunk in f.chunks():
            destination.write(chunk)
