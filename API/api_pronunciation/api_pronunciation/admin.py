from django.contrib.admin import AdminSite


class MainAdminSite(AdminSite):
    site_header = 'Pronunciation trainer administration'
    site_title = 'Admin panel'
    index_title = 'Pronunciation trainer admin'


admin_site = MainAdminSite(name='main_admin')
