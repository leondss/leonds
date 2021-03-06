/* global NexT: true */

$(document).ready(function () {
    initScrollSpy();
    initToc();

    function initScrollSpy() {
        var tocSelector = '.post-toc';
        var $tocElement = $(tocSelector);
        var activeCurrentSelector = '.active-current';

        $tocElement
            .on('activate.bs.scrollspy', function () {
                var $currentActiveElement = $(tocSelector + ' .active').last();

                removeCurrentActiveClass();
                $currentActiveElement.addClass('active-current');

                // Scrolling to center active TOC element if TOC content is taller then viewport.
                $tocElement.scrollTop($currentActiveElement.offset().top - $tocElement.offset().top + $tocElement.scrollTop() - ($tocElement.height() / 2));
            })
            .on('clear.bs.scrollspy', removeCurrentActiveClass);

        $('body').scrollspy({target: tocSelector});

        function removeCurrentActiveClass() {
            $(tocSelector + ' ' + activeCurrentSelector)
                .removeClass(activeCurrentSelector.substring(1));
        }
    }

    function initToc() {
        var $ele = $('#postsBody');
        if ($ele) {
            var headingsMaxDepth = 6;
            var headingsSelector = ['h1', 'h2', 'h3', 'h4', 'h5', 'h6'].slice(0, headingsMaxDepth).join(',');
            var headings = $ele.find(headingsSelector);

            if (!headings.length) return '';

            var className = 'nav';
            var listNumber = true;
            var result = '<ol class="' + className + '">';
            var lastNumber = [0, 0, 0, 0, 0, 0];
            var firstLevel = 0;
            var lastLevel = 0;

            function getId(ele) {
                return $(ele).find('a').attr('name');
            }

            headings.each(function () {
                var level = +this.tagName[1];
                var id = getId(this);
                var text = $(this).text();

                lastNumber[level - 1]++;

                for (var i = level; i <= 5; i++) {
                    lastNumber[i] = 0;
                }

                if (firstLevel) {
                    for (var i = level; i < lastLevel; i++) {
                        result += '</li></ol>';
                    }

                    if (level > lastLevel) {
                        result += '<ol class="' + className + '-child">';
                    } else {
                        result += '</li>';
                    }
                } else {
                    firstLevel = level;
                }

                result += '<li class="' + className + '-item ' + className + '-level-' + level + '">';
                result += '<a class="' + className + '-link" href="#' + id + '">';

                if (listNumber) {
                    result += '<span class="' + className + '-number">';

                    for (var i = firstLevel - 1; i < level; i++) {
                        result += lastNumber[i] + '.';
                    }

                    result += '</span> ';
                }

                result += '<span class="' + className + '-text">' + text + '</span></a>';

                lastLevel = level;
            });

            for (var i = firstLevel - 1; i < lastLevel; i++) {
                result += '</li></ol>';
            }

            $('.post-toc-content').append(result);
        }
    }

});

$(document).ready(function () {
    var html = $('html');
    var TAB_ANIMATE_DURATION = 200;
    var hasVelocity = $.isFunction(html.velocity);

    $('.sidebar-nav li').on('click', function () {
        var item = $(this);
        var activeTabClassName = 'sidebar-nav-active';
        var activePanelClassName = 'sidebar-panel-active';
        if (item.hasClass(activeTabClassName)) {
            return;
        }

        var currentTarget = $('.' + activePanelClassName);
        var target = $('.' + item.data('target'));

        hasVelocity ?
            currentTarget.velocity('transition.slideUpOut', TAB_ANIMATE_DURATION, function () {
                target
                    .velocity('stop')
                    .velocity('transition.slideDownIn', TAB_ANIMATE_DURATION)
                    .addClass(activePanelClassName);
            }) :
            currentTarget.animate({opacity: 0}, TAB_ANIMATE_DURATION, function () {
                currentTarget.hide();
                target
                    .stop()
                    .css({'opacity': 0, 'display': 'block'})
                    .animate({opacity: 1}, TAB_ANIMATE_DURATION, function () {
                        currentTarget.removeClass(activePanelClassName);
                        target.addClass(activePanelClassName);
                    });
            });

        item.siblings().removeClass(activeTabClassName);
        item.addClass(activeTabClassName);
    });

    // TOC item animation navigate & prevent #item selector in adress bar.
    $('.post-toc a').on('click', function (e) {
        e.preventDefault();
        var targetSelector = NexT.utils.escapeSelector(this.getAttribute('href'));
        var selector = 'a[name="' + targetSelector.replace('#', '') + '"'
        var offset = $(selector).offset().top;

        hasVelocity ?
            html.velocity('stop').velocity('scroll', {
                offset: offset + 'px',
                mobileHA: false
            }) :
            $('html, body').stop().animate({
                scrollTop: offset
            }, 500);
    });

    // Expand sidebar on post detail page by default, when post has a toc.
    var $tocContent = $('.post-toc-content');
    var isSidebarCouldDisplay = CONFIG.sidebar.display === 'post' ||
        CONFIG.sidebar.display === 'always';
    var hasTOC = $tocContent.length > 0 && $tocContent.html().trim().length > 0;
    if (isSidebarCouldDisplay && hasTOC) {
        CONFIG.motion.enable ?
            (NexT.motion.middleWares.sidebar = function () {
                NexT.utils.displaySidebar();
            }) : NexT.utils.displaySidebar();
    }
});
