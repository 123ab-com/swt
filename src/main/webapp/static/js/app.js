(function () {
    'use strict';

    function ctx() {
        return window.CTX || '';
    }

    function handleResponse(resp) {
        if (resp.status === 401) {
            alert('登录已失效，请重新登录');
            window.location.href = ctx() + '/login';
            return {code: 401, msg: '未登录'};
        }
        return resp.json().catch(function () {
            return {code: resp.status, msg: '服务器返回异常（HTTP ' + resp.status + '）'};
        });
    }

    /** 分页跳转，保留当前查询条件 */
    window.goPage = function (pageNum) {
        if (!pageNum || pageNum < 1) {
            return;
        }
        var url = new URL(window.location.href);
        url.searchParams.set('pageNum', pageNum);
        window.location.href = url.toString();
    };

    /** 表单方式提交 AJAX */
    window.postForm = function (url, data) {
        var body = new URLSearchParams();
        Object.keys(data || {}).forEach(function (key) {
            body.append(key, data[key]);
        });
        return fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: body.toString()
        }).then(handleResponse);
    };

    /** JSON 方式提交 AJAX */
    window.postJson = function (url, data) {
        return fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: JSON.stringify(data)
        }).then(handleResponse);
    };

    /** 通用删除确认 */
    window.confirmPost = function (url, data, message) {
        if (!confirm(message || '确定要执行该操作吗？')) {
            return Promise.resolve(null);
        }
        return postForm(url, data).then(function (res) {
            alert(res.msg);
            if (res.code === 200) {
                window.location.reload();
            }
            return res;
        });
    };
})();
