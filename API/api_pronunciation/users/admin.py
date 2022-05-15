from django.contrib import admin
from api_pronunciation.admin import admin_site
from users.models import User


class UserAdmin(admin.ModelAdmin):
    list_display = ['id', 'username', 'email', 'view_first_name', 'view_last_name',
                    'average_score', 'pronounced', 'date_joined', 'is_superuser', 'is_staff']
    list_filter = ['is_superuser', 'is_staff']
    search_fields = ['username', 'email', 'first_name', 'last_name']
    ordering = ['-date_joined']

    exclude = ['password']

    def view_first_name(self, obj):
        if len(obj.first_name) == 0:
            return '-'
        return obj.first_name
    view_first_name.short_description = 'First Name'

    def view_last_name(self, obj):
        if len(obj.last_name) == 0:
            return '-'
        return obj.last_name

    view_last_name.short_description = 'Last Name'


admin_site.register(User, UserAdmin)
