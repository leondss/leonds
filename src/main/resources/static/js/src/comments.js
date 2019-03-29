$(function () {
    var Comments = function (element) {
        this.$element = element;
    };

    Comments.prototype.init = function () {
        if (this.$element) {
            this.subjectId = this.$element.data('subject-id');
            this.commentsType = this.$element.data('comments-type');
            this.initLayout();
            this.getCommentsCount();
            this.getComments();
        }
    };

    Comments.prototype.initLayout = function () {
        var html = '<div class="comments-wrap">\n' +
            '                    <div class="ct-meta"><span class="ct-counts"></span> 条评论</div>\n' +
            '                    <div class="ct-body">\n' +
            '                    </div>\n' +
            '                   <div class="pagination">\n' +
            '                    <ul id="pagination" class="pagination-sm"></ul>\n' +
            '                   </div>' +
            '                        <form class="ct-form" id="commentForm">\n' +
            '                            <div>\n' +
            '                                <textarea maxlength="1000" rows="3" placeholder="在这里评论" name="content"></textarea>\n' +
            '                            </div>\n' +
            '                            <div>\n' +
            '                                <input type="text" placeholder="昵称" name="nickName">\n' +
            '                                <input type="text" placeholder="邮箱" name="email">\n' +
            '                                <input type="text" placeholder="网址" name="site">\n' +
            '                                <button type="button" class="btn pull-right">发表评论</button>\n' +
            '                                <span class="pull-right" style="margin-right: 10px">0/1000</span>\n' +
            '                            </div>\n' +
            '                        </form>\n' +
            '                    </div>\n' +
            '                </div>';

        this.$element.append(html);

        this.$comments = $('.comments-wrap .ct-body');

        this.bindEvents('#commentForm');

        this.initCommentsUser('#commentForm');

    };

    Comments.prototype.getComments = function (page) {
        var self = this;
        var currentPage = page ? page - 1 : 0;
        var params = {
            subjectId: this.subjectId,
            page: currentPage
        };
        $.get('/fpi/comments/list', params, function (res) {
            self.$comments.empty();
            if (res.rows.length > 0) {
                for (var i = 0; i < res.rows.length; i++) {
                    var row = res.rows[i];
                    var time = self.times(row.publishTime);
                    var floor = res.total - i;
                    var html = '<div class="comment">' +
                        '   <div class="comment-meta">\n' +
                        '       <a href="' + row.site + '" target="_blank">' + row.nickName + '</a>\n' +
                        '       <span>' + time + '</span>\n' +
                        '       <span class="comment-floor">#' + floor + '</span>' +
                        '   </div>\n' +
                        '   <div class="comment-text">\n';
                    if (row.relComments) {
                        html += '<div class="rel">' + row.relComments + '</div>';
                    }
                    html += row.content +
                        '   </div>\n' +
                        '   <div class="comment-actions">\n' +
                        '       <a class="comment-reply" data-id="' + row.id + '"><i class="fa fa-comment-o"></i> 回复</a>\n' +
                        '   </div>\n' +
                        '</div>';
                    self.$comments.append(html);
                }
                self.initReply();
            } else {
                self.$comments.append('暂无评论~');
            }
        });
    };

    Comments.prototype.getCommentsCount = function (auto) {
        var self = this;
        $.get('/fpi/comments/count', {subjectId: self.subjectId}, function (count) {
            $('.ct-meta .ct-counts').html(count);
            $('.post-comments-count .disqus-comment-count').html(count);
            self.initPagination(count, auto);
        });
    };

    Comments.prototype.initPagination = function (total, auto) {
        var totalPages = (total / 10) + (total % 10 > 0 ? 1 : 0);
        if (total > 0) {
            var self = this;
            this.$pagination = $('#pagination');
            this.$pagination.twbsPagination('destroy');
            this.$pagination.twbsPagination({
                totalPages: totalPages || 1,
                visiblePages: 7,
                initiateStartPageClick: auto || false,
                first: null,
                last: null,
                prev: '<',
                next: '>',
                onPageClick: function (event, page) {
                    console.log(page);
                    self.scrollComments();
                    self.getComments(page);
                }
            });
        }
    };

    Comments.prototype.times = function (date) {
        var dateBegin = new Date(date.replace(/-/g, "/"));//将-转化为/，使用new Date
        var dateEnd = new Date();//获取当前时间
        var dateDiff = dateEnd.getTime() - dateBegin.getTime();//时间差的毫秒数
        var dayDiff = Math.floor(dateDiff / (24 * 3600 * 1000));//计算出相差天数
        var leave1 = dateDiff % (24 * 3600 * 1000);    //计算天数后剩余的毫秒数
        var hours = Math.floor(leave1 / (3600 * 1000));//计算出小时数
        //计算相差分钟数
        var leave2 = leave1 % (3600 * 1000);    //计算小时数后剩余的毫秒数
        var minutes = Math.floor(leave2 / (60 * 1000));//计算相差分钟数
        //计算相差秒数
        var timesString = '';

        if (dayDiff !== 0) {
            if (dayDiff > 5) {
                timesString = date.substring(0, 16);
            } else {
                timesString = dayDiff + '天前';
            }
        } else if (dayDiff === 0 && hours !== 0) {
            timesString = hours + '小时前';
        } else if (dayDiff === 0 && hours === 0) {
            if (minutes > 1) {
                timesString = minutes + '分钟前';
            } else {
                timesString = '刚刚';
            }
        }
        return timesString;
    };

    Comments.prototype.bindEvents = function (selector) {
        var self = this;

        // 文本域
        $(selector).find('textarea').bind('keyup', function () {
            $(selector).find('span').html($(this).val().length + '/1000')
        });

        // 评论按钮
        $(selector + ' button').click(function () {
            var content = $(selector + ' textarea[name="content"]').val();
            if (!content) {
                alert('请输入评论内容');
                return;
            }
            var nickName = $(selector + ' input[name="nickName"]').val();
            if (!nickName) {
                alert('请输入昵称');
                return;
            }
            var email = $(selector + ' input[name="email"]').val();
            if (!email) {
                alert('请输入邮箱地址');
                return;
            }
            var site = $(selector + ' input[name="site"]').val();

            var relId = $(this).data('rel-id');

            var params = {
                content: content,
                nickName: nickName,
                email: email,
                site: site,
                subjectId: self.subjectId,
                subjectType: self.commentsType,
                pid: relId
            };

            $.ajax({
                type: "POST",
                url: "/fpi/comments",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(params),
                dataType: "json",
                success: function (res) {
                    if (res.status === 0) {
                        Cookies.set('user', JSON.stringify({
                            nickName: params.nickName,
                            email: params.email,
                            site: params.site
                        }));
                        self.getCommentsCount(true);
                        $(selector + ' textarea[name="content"]').val('');
                        $(selector + ' textarea[name="content"]').trigger('keyup');
                    } else {
                        alert(res.message);
                    }
                }
            });
        })
    };

    Comments.prototype.initCommentsUser = function (selector) {
        var user = Cookies.getJSON('user');
        if (user) {
            $(selector + ' input[name="nickName"]').val(user.nickName);
            $(selector + ' input[name="email"]').val(user.email);
            $(selector + ' input[name="site"]').val(user.site);
        }
    };

    Comments.prototype.scrollComments = function () {
        $('html, body').animate({
            scrollTop: $("#comments").offset().top
        }, 800);
    };

    Comments.prototype.initReply = function () {
        var self = this;
        var seletor = '#commentsReplForm';
        $('.comment-reply').click(function () {
            $(seletor).remove();
            var id = $(this).data('id');
            $(this).parent().after('<div id="commentsReplForm"><form class="ct-form">\n' +
                '                            <div>\n' +
                '                                <textarea maxlength="1000" rows="3" placeholder="在这里评论" name="content"></textarea>\n' +
                '                            </div>\n' +
                '                            <div>\n' +
                '                                <input type="text" placeholder="昵称" name="nickName">\n' +
                '                                <input type="text" placeholder="邮箱" name="email">\n' +
                '                                <input type="text" placeholder="网址" name="site">\n' +
                '                                <a>关闭</a>\n' +
                '                                <button type="button" data-rel-id="' + id + '" class="btn pull-right">回复</button>\n' +
                '                                <span class="pull-right" style="margin-right: 10px">0/1000</span>\n' +
                '                            </div>\n' +
                '                        </form></div>');

            self.bindEvents(seletor);
            self.initCommentsUser(seletor);
        });
    };

    //
    new Comments($('#comments')).init();

});