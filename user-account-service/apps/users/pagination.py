from rest_framework.pagination import CursorPagination


class UserPagination(CursorPagination):
    page_size = 20
    ordering = "id"
    cursor_query_param = "cursor"
    page_size_query_param = "page"
    max_page_size = 100
