from django.urls import path, include, re_path

from users.views import Register, Login

urlpatterns = [
    path('register/', Register.as_view()),
    path('login/', Login.as_view()),

]
