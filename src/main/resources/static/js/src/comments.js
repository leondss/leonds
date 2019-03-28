$(function () {
    var Comments = function (element) {
        this.$element = element
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

    Comments.prototype.getPage = function () {
        var page = this.$element.data('page');
        return page ? Number(page) : 0;
    };

    Comments.prototype.setPage = function (page) {
        this.$element.data('page', page);
    };

    Comments.prototype.initLayout = function () {
        var html = '<div class="comments-wrap">\n' +
            '                    <div class="ct-meta"><span class="ct-counts"></span> 条评论</div>\n' +
            '                    <div class="ct-body">\n' +
            '                    </div>\n' +
            '                    <div class="ct-footer">\n' +
            '                        <nav class="pagination">\n' +
            '                            <span class="page-number current">1</span><a class="page-number" href="/page/2/">2</a><a\n' +
            '                                class="extend next" rel="next" href="/page/2/">下一页</a>\n' +
            '                        </nav>\n' +
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

        this.bindCalcContentLength('#commentForm');

        this.bindSubmitComments('#commentForm');

        this.initCommentsUser('#commentForm');
    };

    Comments.prototype.getComments = function () {
        var self = this;
        var params = {
            subjectId: this.subjectId,
            page: Number(this.$element.data('page')) - 1
        };
        $.get('/fpi/comments/list', params, function (res) {
            self.initComments(res);
            self.initPagination(res);
        });
    };

    Comments.prototype.getCommentsCount = function () {
        var self = this;
        $.get('/fpi/comments/count', {subjectId: self.subjectId}, function (count) {
            $('.ct-meta .ct-counts').html(count);
        });
    };

    Comments.prototype.initComments = function (res) {
        var self = this;
        self.$comments.empty();
        if (res.rows.length > 0) {
            for (var i = 0; i < res.rows.length; i++) {
                var row = res.rows[i];
                var time = self.times(row.publishTime)
                var floor = res.total - i
                var html = '<div class="comment">' +
                    '                            <div class="comment-meta">\n' +
                    '                                <a href="' + row.site + '" target="_blank">' + row.nickName + '</a>\n' +
                    '                                <span>' + time + '</span>\n' +
                    '                                <span class="comment-floor">#' + floor + '</span>' +
                    '                            </div>\n' +
                    '                            <div class="comment-text">\n';
                if (row.relComments) {
                    html += '<div class="rel">' + row.relComments + '</div>';
                }
                html += row.content +
                    '                            </div>\n' +
                    '                            <div class="comment-actions">\n' +
                    '                                <a class="comment-reply" data-id="' + row.id + '"><i class="fa fa-comment-o"></i> 回复</a>\n' +
                    '                            </div>\n' +
                    '                        </div>';
                self.$comments.append(html);
            }
        } else {
            self.$comments.append('暂无评论~');
        }
    };

    Comments.prototype.initPagination = function (res) {
        var $ele = $('.pagination');
        var self = this;
        var currentPage = this.getPage();
        $ele.empty();
        if (res.rows.length > 0) {
            for (var i = 1; i <= res.totalPages; i++) {
                if (i === currentPage) {
                    $ele.append($('<span class="page-number current">' + i + '</span>'));
                } else {
                    var $pageNumber = $('<a class="page-number" data-page="' + i + '">' + i + '</a>')
                    $pageNumber.bind('click', function () {
                        self.setPage($(this).data('page'));
                        self.getComments();
                    });
                    $ele.append($pageNumber)
                }
            }
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

    Comments.prototype.bindCalcContentLength = function (selector) {
        $(selector).find('textarea').bind('keyup', function () {
            $(selector).find('span').html($(this).val().length + '/1000')
        });
    };

    Comments.prototype.bindSubmitComments = function (selector) {
        var self = this;
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

            var relId = $(this).data('relId');

            var params = {
                content: content,
                nickName: nickName,
                email: email,
                site: site,
                subjectId: self.id,
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
                        alert('评论成功!');
                        $(selector + ' textarea[name="content"]').val('');
                        self.getComments();
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

    //
    new Comments($('#comments')).init();

});