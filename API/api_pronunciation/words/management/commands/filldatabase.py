import os

from django.core.files import File
from django.core.management import BaseCommand
from os import listdir
from os.path import isfile, join

from api_pronunciation import settings
from words.models import Word


class Command(BaseCommand):
    def handle(self, *args, **options):
        Word.objects.all().delete()

        files_list = [f for f in listdir(settings.STATIC_AUDIO) if isfile(join(settings.STATIC_AUDIO, f))]
        for file_path in files_list:
            new_word = Word(
                word=file_path.split('.')[0],
                transcription='[...]',
                audio_path=os.path.join(settings.STATIC_AUDIO, file_path),
            )
            new_word.save()
