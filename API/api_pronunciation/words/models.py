from django.db import models


class Word(models.Model):
    word = models.CharField(primary_key=True, db_index=True, unique=True, max_length=128)
    transcription = models.CharField(max_length=128)
    audio_path = models.FilePathField(max_length=512, default=None, path='static/words_records')
