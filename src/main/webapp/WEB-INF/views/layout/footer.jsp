            </div>
        </div>
    </div>
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/5.3.1/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.bootcdn.net/ajax/libs/font-awesome/6.4.2/js/all.min.js"></script>
    <script>
        // 高亮当前活动的导航项
        $(document).ready(function() {
            var currentUrl = window.location.pathname;
            $('.nav-link').each(function() {
                if (currentUrl.indexOf($(this).attr('href')) !== -1) {
                    $(this).addClass('active');
                }
            });
        });
    </script>
</body>
</html>
