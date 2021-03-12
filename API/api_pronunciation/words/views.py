import os

from django.http import HttpResponse
from django.shortcuts import render
from rest_framework import generics, views
from rest_framework.generics import GenericAPIView
from rest_framework.parsers import FileUploadParser
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.views import APIView

from words.models import Word
from words.serializers import WordDetailSerializer
from api_pronunciation import settings


class FileUploadView(views.APIView):
    parser_classes = [FileUploadParser]

    def put(self, request, filename, format=None):
        file_obj = request.data['file']
        # ...
        # do some stuff with uploaded file
        # ...
        return Response(status=204)


class AllWordsView(generics.ListAPIView):
    serializer_class = WordDetailSerializer
    queryset = Word.objects.all()


class WordDetailView(generics.RetrieveAPIView):
    serializer_class = WordDetailSerializer
    queryset = Word.objects.all()
    permission_classes = (IsAuthenticated,)


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
