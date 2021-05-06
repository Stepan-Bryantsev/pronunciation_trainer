from rest_framework.parsers import FileUploadParser, MultiPartParser


class AudioUploadParser(MultiPartParser):
    media_type = 'audio/mp3'
