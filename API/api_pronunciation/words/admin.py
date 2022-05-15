from django.contrib import admin
from api_pronunciation.admin import admin_site

from words.models import Word


class WordAdmin(admin.ModelAdmin):
    list_display = ['word', 'transcription', 'audio_path']
    search_fields = ['word', 'audio_path']
    ordering = ['word']


admin_site.register(Word, WordAdmin)
