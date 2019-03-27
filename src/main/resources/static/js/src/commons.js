var Services = {
    random: function (min, max) {
        return parseInt(Math.random() * (max - min + 1) + min, 10);
    },
    getTags: function () {
        $.get('/fpi/tags', function (res) {
            if (res) {
                var colors = ['#8e8e8e', '#ccc', '#4f4f4f', '#bcbcbc', '#404040', '#111', '#5f5f5f']
                var html = ''
                $('.tag-cloud-title').html('目前共计 ' + res.length + ' 个标签')
                for (var i = 0; i < res.length; i++) {
                    var tag = res[i]
                    var size = 12 + Services.random(0, 5)
                    var color = colors[Services.random(0, 6)]
                    html += '<a href="/tag/' + tag.name + '" style="font-size: ' + size + 'px; color: ' + color + '">' + tag.name + '</a>'
                }
                $('.tag-cloud-tags').append(html)
            }
        })
    },
    getIndexCount: function () {
        $.get('/fpi/rpt', function (res) {
            if (res) {
                $('#countCate').text(res['cateCount']);
                $('#countPosts').text(res['postsCount']);
                $('#countTag').text(res['tagCount']);
            }
        })
    },
    activeMenu: function () {
        $('#menu a').each(function () {
            var href = $(this).attr('href')
            if (href === location.pathname) {
                $(this).parent().addClass('menu-item-active').siblings().removeClass('menu-item-active');
            }
        })
    },
    getLinks: function () {
        $.get('/fpi/links', function (res) {
            if (res) {
                var html = ''
                for (var i = 0; i < res.length; i++) {
                    var item = res[i]
                    html += '<li class="links-of-blogroll-item">\n' +
                        '   <a href="' + item.siteUrl + '" title="' + item.siteUrl + '" rel="noopener" target="_blank">' + item.siteName + '</a>\n' +
                        '</li>'
                }
                $('#links').append(html)
            }
        })
    },
    getComments: function (subjectId, page, size) {
        $.get('/fpi/comments/list?page=' + page + '&size=' + size + '&subjectId=' + subjectId, function (res) {
            if (res) {
                console.log(res);
            }
        })
    },
    initReply: function (target) {
        var tpl =
            '<div id="commentsReply"><form class="ct-form">\n' +
            '       <div>\n' +
            '           <textarea maxlength="1000" rows="3" placeholder="在这里评论" name="content"></textarea>\n' +
            '       </div>\n' +
            '       <div>\n' +
            '           <input type="text" placeholder="昵称" name="nickName">\n' +
            '           <input type="text" placeholder="邮箱" name="email">\n' +
            '           <input type="text" placeholder="网址" name="site">\n' +
            '           <button type="button" class="btn pull-right">发表评论</button>\n' +
            '           <span class="pull-right" style="margin-right: 10px">0/1000</span>\n' +
            '       </div>\n' +
            '</form></div>';
        var id = target.data('id');
        var parent = target.parent();
    }
}


$(function () {
    Services.getIndexCount();
    Services.activeMenu();
    Services.getLinks();
})