/**
 * Created by manabu on 2016/11/11.
 */

(function() {
    var e,
        a,
        t,
        r,
        n,
        o,
        i,
        l,
        s,
        c,
        p,
        u,
        d,
        _,
        f,
        h,
        g,
        m = [].indexOf || function(e) {
                for (var a = 0, t = this.length; t > a; a++)
                    if (a in this && this[a] === e)
                        return a;
                return -1
            };
    l = this, $(function() {
        var e,
            a,
            t,
            r,
            n,
            o,
            i,
            s,
            p,
            v,
            y,
            k,
            b,
            C,
            x,
            w,
            j,
            T;
        for ($(".selectpicker").selectpicker("mobile"), b = !1, l.precision_icons = {}, x = ["A", "B", "C"], n = 0, p = x.length; p > n; n++)
            C = x[n], l.precision_icons[C] = L.icon({
                iconUrl: l.precision_icon_path(C),
                iconSize: [31, 41],
                iconAnchor: [15, 40],
                popupAnchor: [0, -32]
            });
        (a = $("#last_search")) && (o = a.val()), $("#aed_table").is(":visible") && (t = function() {
            $("#aeds").empty(), h({
                first: 0,
                last: 0,
                total: 0,
                total_pages: 0,
                current_page: 0
            })
        }, d(g, t), $("#pager_pages, #pager_pages_below").on("click", function(e) {
            var a,
                t;
            e.preventDefault(), a = $(e.target), t = a.data("page"), "undefined" != typeof t && t !== Number($("#page").val()) && (_(t), $("#" + l.last_search).submit()), "pager_pages_below" === e.currentTarget.id && $(e.currentTarget).data("scroll", !0)
        }), $("input[type=submit]").on("click", function() {
            b && u("aed_results")
        }), o ? $("#" + o).submit() : $.ajax({
            url: "/api/aed/list",
            dataType: "json",
            success: g
        })), $("#search_keyword").is(":visible") && (l.last_search = "search_all", e = $("#collapse-form"), $("input[type=submit]").on("click", function() {
            _(1), e.collapse("hide")
        }), $("#collapse-form").on("show.bs.collapse", function() {
            return $("#collapse-icon").addClass("glyphicon-collapse-up").removeClass("glyphicon-collapse-down")
        }), $("#collapse-form").on("hide.bs.collapse", function() {
            return $("#collapse-icon").addClass("glyphicon-collapse-down").removeClass("glyphicon-collapse-up")
        })), $("#aed_map").is(":visible") && (y = create_leaflet_map("aed_map"), v = 139.766865, i = 35.681109, y.setView([i, v], 10), l.map = y, s = l.marker_layer || (l.marker_layer = L.layerGroup()), l.markerClusterGroup = L.markerClusterGroup({
            showCoverageOnHover: !1,
            maxClusterRadius: 10
        }), s.addTo(l.map), l.markerClusterStatus = !1, l.map.addControl(new l.SwitchClusterControl), f(v, i), w = function() {
            var e;
            return e = l.map.getCenter(), f(e.lng, e.lat)
        }, $("#search_keyword, #search_all, #search_condition").on("ajax:before", w), T = function(e, a) {
            var t,
                r,
                n,
                o,
                i,
                p;
            for (c(), b && u("aed_map"), p = a.aeds, r = 0, o = p.length; o > r; r++)
                t = p[r], n = t.location, i = l.create_marker(n.longitude, n.latitude, l.precision_icon(t.rank)), l.add_popup(i, l.popup_contents(t)), l.set_click_listener_to_marker(i, t), s.addLayer(i), l.markerClusterGroup.addLayer(i)
        }, r = function() {
            c(), b && u("aed_map"), l.marker_layer && l.marker_layer.clearLayers()
        }, d(T, r), $("#aed_map").data("realtime") === !0 && (l.aeds = [], j = function() {
            var e,
                a;
            return e = l.map.getCenter(), a = {
                latitude: e.lat,
                longitude: e.lng
            }, $.ajax({
                url: "/api/aed/search_by_location",
                dataType: "json",
                type: "GET",
                data: a
            }).done(function(e) {
                var a,
                    t,
                    r,
                    n,
                    o,
                    i,
                    c;
                for (s = l.marker_layer, i = e.aeds, t = 0, r = i.length; r > t; t++)
                    a = i[t], c = a.id, m.call(l.aeds, c) < 0 && (n = a.location, o = l.create_marker(n.longitude, n.latitude, l.precision_icon(a.rank)), l.add_popup(o, l.popup_contents(a)), l.set_click_listener_to_marker(o, a), s.addLayer(o), l.markerClusterGroup.addLayer(o), l.aeds.push(a.id))
            })
        }, y.on("moveend", function() {
            return j()
        }), k = function(e) {
            var a;
            a = L.latLng(e.coords.latitude, e.coords.longitude), l.map.setView(a, 18, {
                animate: !0
            })
        }, navigator.geolocation && navigator.geolocation.getCurrentPosition(k)), $("#aed_install_prefecture_id").is(":visible") && $("#aed_install_prefecture_id").on("change", function(e) {
            var a,
                t;
            return t = $(e.currentTarget).val(), a = $("#prefecture_" + t), v = a.data("longitude"), i = a.data("latitude"), l.map.setView([i, v])
        }))
    }), f = function(e, a) {
        return $("input[name='location[longitude]']").val(e), $("input[name='location[latitude]']").val(a)
    }, c = function() {
        $(".aed_detail_table td[id!=rank_description]").text("")
    }, p = function(e) {
        "search_keyword" === e ? $("#search_condition")[0].reset() : "search_condition" === e ? $("#search_keyword")[0].reset() : ($("#search_keyword")[0].reset(), $("#search_condition")[0].reset()), $(".selectpicker").selectpicker("render")
    }, _ = function(e) {
        $("input[name=page]").each(function(a, t) {
            $(t).val(e)
        })
    }, d = function(e, a) {
        var t,
            r,
            n;
        r = function(a, t) {
            var r,
                n;
            n = $(a.target).attr("id"), p(n), l.last_search = "" === l.last_search ? "" : n, (r = $("#last_search")) && r.val(n), t.pages && _(t.pages.current_page), "function" == typeof e && e.call(this, a, t)
        }, t = function(e, t, r, n) {
            var o;
            null !== e && (o = $(e.target).attr("id"), p(o), l.last_search = "" === l.last_search ? "" : o), _(1), "function" == typeof a && a.call(this, e, t, r, n)
        }, n = $("#search_keyword, #search_condition, #search_all"), n.off("ajax:success").on("ajax:success", r), n.off("ajax:error").on("ajax:error", t)
    }, g = function(e, a) {
        var t,
            r,
            n,
            o,
            i,
            l,
            s,
            c;
        for (e.aeds && Array.isArray(e.aeds) && "string" == typeof a && (a = e), i = $("#aeds"), i.empty(), r = a.aeds, c = [], n = 0; n < r.length;)
            t = r[n], s = $("<tr>"), l = [], l.push($("<td>").text(t.install_prefecture_name || "")), l.push($("<td>").text(t.install_location_name || "")), o = $("<a>").text(t.install_address || ""), t.location && t.location.latitude && t.location.longitude && o.attr("href", "/map/my_map?latitude=" + t.location.latitude + "&longitude=" + t.location.longitude + "&zoom=16&id=" + t.id), l.push($("<td>").append(o)), l.push($("<td>").text(t.install_address_detail || "")), l.push($("<td>").text(t.register_number || "")), s.append(l), c.push(s), n++;
        i.append(c), h(a.pages)
    }, h = function(l) {
        var s,
            c,
            p,
            d,
            _,
            f,
            h,
            g,
            m,
            v,
            y;
        if (_ = e(l.first, l.last, l.total), $("#entries_info").text(_), $("#entries_info_below").text(_), y = l.total_pages, p = l.current_page, v = r(y, p), s = $("#pager_pages"), c = $("#pager_pages_below"), s.empty(), c.empty(), 0 !== l.total_pages) {
            for (d = [], d.push(a(p)), 1 !== v[0] && d.push(o()), f = h = 0, g = v.length; g > h; f = ++h)
                m = v[f], d.push(n(m, m === p));
            return v[v.length - 1] !== y && d.push(o()), d.push(t(p, y)), s.append(d), c.append(i(d)), c.data("scroll") ? (u("aed_results", !1), c.data("scroll", !1)) : void 0
        }
    }, a = function(e) {
        var a,
            t;
        return t = $("<li>"), 1 === e && t.addClass("disabled"), a = $("<a>").text("\u5148\u982d").data("page", 1), t.append(a)
    }, t = function(e, a) {
        var t,
            r;
        return r = $("<li>"), e === a && r.addClass("disabled"), t = $("<a>").text("\u6700\u7d42").data("page", a), r.append(t)
    }, o = function() {
        var e,
            a;
        return a = $("<li>").addClass("disabled"), e = $("<a>").text("..."), a.append(e)
    }, n = function(e, a) {
        var t,
            r;
        return null == a && (a = !1), r = $("<li>"), a && r.addClass("active"), t = $("<a>").text(e).data("page", e), r.append(t)
    }, e = function(e, a, t) {
        return 0 === t ? "\u8a72\u5f53\u3059\u308b\u30c7\u30fc\u30bf\u306f\u3042\u308a\u307e\u305b\u3093\u3067\u3057\u305f" : e + "-" + a + "\u4ef6\u3092\u8868\u793a\u4e2d / \u5408\u8a08" + t + "\u4ef6"
    }, r = function(e, a) {
        var t,
            r,
            n,
            o;
        return 1 === e ? t = [1] : 6 > e ? t = function() {
            n = [];
            for (var a = 1; e >= 1 ? e >= a : a >= e; e >= 1 ? a++ : a--)
                n.push(a);
            return n
        }.apply(this) : (t = function() {
            o = [];
            for (var e = r = a - 2, t = a + 2; t >= r ? t >= e : e >= t; t >= r ? e++ : e--)
                o.push(e);
            return o
        }.apply(this), t = t.filter(function(e) {
            return e > 0
        }), t = t.filter(function(a) {
            return e >= a
        })), t
    }, i = function(e) {
        var a,
            t,
            r,
            n;
        for (a = [], t = 0, r = e.length; r > t; t++)
            n = e[t], a.push(n.clone(!0));
        return a
    }, u = function(e, a) {
        var t,
            r,
            n;
        t = 800, a === !1 && (t = 0), r = $("#" + e), n = r.offset().top, $("html,body").animate({
            scrollTop: n - 20
        }, t)
    }, s = function() {
        return $(".container").width() >= 970
    }
}).call(this);

