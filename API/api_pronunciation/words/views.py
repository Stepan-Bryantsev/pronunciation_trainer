import os
import random

from django.http import HttpResponse
from django.shortcuts import render, redirect
from django.views.generic.base import View
from rest_framework import generics, views, status
from rest_framework.generics import GenericAPIView, RetrieveAPIView, ListAPIView
from rest_framework.pagination import PageNumberPagination
from rest_framework.parsers import FileUploadParser
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.views import APIView

from words.check import check_pronunciation
from words.models import Word
from words.parsers import AudioUploadParser
from words.serializers import WordDetailSerializer
from api_pronunciation import settings


class AllWordsView(ListAPIView):
    serializer_class = WordDetailSerializer
    queryset = Word.objects.all()
    pagination_class = PageNumberPagination


class SearchWordsApiView(ListAPIView):
    serializer_class = WordDetailSerializer
    #permission_classes = (IsAuthenticated,)

    def get_queryset(self):
        return Word.objects.filter(word__contains=self.kwargs['search_word'])


class WordDetailView(RetrieveAPIView):
    serializer_class = WordDetailSerializer
    queryset = Word.objects.all()
    #permission_classes = (IsAuthenticated,)


class RandomWordView(RetrieveAPIView):
    serializer_class = WordDetailSerializer
    queryset = Word.objects.all()
    #permission_classes = (IsAuthenticated,)

    def get_object(self):
        return random.choice(self.get_queryset())


class WordAudioView(GenericAPIView):
    serializer_class = WordDetailSerializer
    queryset = Word.objects.all()
    permission_classes = (IsAuthenticated,)

    def get(self, request, *args, **kwargs):

        path = self.get_object().audio_path

        check_pronunciation(request.user, "qwe")

        with open(path, 'rb') as f:
            response = HttpResponse()
            response.write(f.read())
            response['Content-Type'] = 'audio/mp3'
            response['Content-Length'] = os.path.getsize(path)
            return response


class AudioUploadView(APIView):
    parser_class = (AudioUploadParser,)
    permission_classes = (IsAuthenticated,)

    def post(self, request, filename, format=None):
        if 'file' not in request.data:
            return Response(status=400)

        f = request.data['file']

        return Response(status=status.HTTP_200_OK)
