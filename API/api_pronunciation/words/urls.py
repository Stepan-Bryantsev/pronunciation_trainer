from django.urls import path, include, re_path

from words.views import WordDetailView, AllWordsView, WordAudioView, CheckPronunciationView, SearchWordsApiView, RandomWordView

urlpatterns = [
    path('word/<str:pk>/', WordDetailView.as_view()),
    path('word/', RandomWordView.as_view()),
    path('word/<str:pk>/audio/', WordAudioView.as_view()),
    path('word/<str:pk>/check/', CheckPronunciationView.as_view()),
    path('words/all/', AllWordsView.as_view()),
    path('words/search/<str:search_word>/', SearchWordsApiView.as_view()),
]
