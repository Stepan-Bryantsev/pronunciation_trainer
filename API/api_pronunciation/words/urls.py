from django.urls import path, include, re_path

from words.views import WordDetailView, AllWordsView, FileUploadView, WordAudioView

urlpatterns = [
    path('word/<str:pk>/', WordDetailView.as_view()),
    path('word/audio/<str:pk>/', WordAudioView.as_view()),
    path('all/', AllWordsView.as_view()),
    re_path(r'^upload/(?P<filename>[^/]+)$', FileUploadView.as_view())
]
