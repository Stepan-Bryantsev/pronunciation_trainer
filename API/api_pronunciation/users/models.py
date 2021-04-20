from django.contrib.auth.models import AbstractUser
from django.utils.translation import gettext_lazy as _
from django.db import models


class User(AbstractUser):
    email = models.EmailField(_('email address'), unique=True)
    average_score = models.PositiveSmallIntegerField(_('average score'), default=0)
    pronounced = models.PositiveBigIntegerField(_('total pronounced'), default=0)
