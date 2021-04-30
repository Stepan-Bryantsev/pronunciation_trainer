from rest_framework import serializers

from words.models import Word


class WordDetailSerializer(serializers.ModelSerializer):
    user = serializers.HiddenField

    class Meta:
        model = Word
        fields = ('word', 'transcription',)
