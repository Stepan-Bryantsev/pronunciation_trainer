from rest_framework.parsers import FileUploadParser


class AudioUploadParser(FileUploadParser):
    media_type = 'audio/mp3'
