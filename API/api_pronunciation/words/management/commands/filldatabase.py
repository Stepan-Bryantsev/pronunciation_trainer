import os

from django.core.files import File
from django.core.management import BaseCommand
from os import listdir
from os.path import isfile, join

from api_pronunciation import settings
from words.models import Word

import requests
import bs4


class Command(BaseCommand):
    def handle(self, *args, **options):
        Word.objects.all().delete()

        files_list = [f for f in listdir(settings.STATIC_AUDIO) if isfile(join(settings.STATIC_AUDIO, f))]
        for file_path in files_list:
            word = file_path.split('.')[0]
            transcription = Command.get_transcription(word)
            new_word = Word(
                word=word,
                transcription='no transcription yet :(' if (transcription == "") else transcription,
                audio_path=os.path.join(settings.STATIC_AUDIO, file_path),
            )
            new_word.save()

    @staticmethod
    def get_transcription(word):
        response = requests.get("https://wooordhunt.ru/word/" + word)
        soup = bs4.BeautifulSoup(response.text, 'lxml')
        div = soup.find("div", id="uk_tr_sound")
        for child in div.recursiveChildGenerator():
            if child.name == "span":
                return child.text.strip().replace('|', '[', 1).replace('|', ']', 1)
