import DashboardLayout from "@/pages/Layout/DashboardLayout.vue";
import Vue from 'vue';
import Router from 'vue-router';

import Login from "@/pages/Login.vue";
import Dashboard from "@/pages/Dashboard.vue";
import UserProfile from "@/pages/UserProfile.vue";
import Locations from "@/pages/Locations.vue";
import TableList from "@/pages/TableList.vue";
import Typography from "@/pages/Typography.vue";
import Icons from "@/pages/Icons.vue";
import Maps from "@/pages/Maps.vue";
import Notifications from "@/pages/Notifications.vue";
import UpgradeToPRO from "@/pages/UpgradeToPRO.vue";

Vue.use(Router)

let router = new Router({
    routes: [
        {
            path: "/",
            component: DashboardLayout,
            meta: {
                requiresAuth: true
            },
            redirect: "/dashboard",
            children: [
                {
                    path: "dashboard",
                    name: "Dashboard",
                    component: Dashboard
                },
                {
                    path: "user",
                    name: "Users",
                    component: UserProfile
                },
              {
                    path: "locations",
                    name: "Locations",
                    component: Locations
                },
                {
                    path: "table",
                    name: "Table List",
                    component: TableList
                },
                {
                    path: "typography",
                    name: "Typography",
                    component: Typography
                },
                {
                    path: "icons",
                    name: "Icons",
                    component: Icons
                },
                {
                    path: "maps",
                    name: "Maps",
                    meta: {
                        hideFooter: true
                    },
                    component: Maps
                },
                {
                    path: "notifications",
                    name: "Notifications",
                    component: Notifications
                },
                {
                    path: "upgrade",
                    name: "Upgrade to PRO",
                    component: UpgradeToPRO
                }
            ]
        },
        {
            path: "/login",
            name: "Login",
            component: Login
        },
    ],
    linkExactActiveClass: "nav-item active"
});

router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (localStorage.getItem('jwt') == null) {
            next({
                path: '/login',
                params: {nextUrl: to.fullPath}
            })
        } else {
            let user = JSON.parse(localStorage.getItem('user'))
            if (to.matched.some(record => record.meta.is_admin)) {
                if (user.is_admin == 1) {
                    next();
                } else {
                    next({name: 'User Profile'});
                }
            } else {
                next();
            }
        }
    } else if (to.matched.some(record => record.meta.guest)) {
        if (localStorage.getItem('jwt') == null) {
            next()
        } else {
            next({name: 'Dashboard'})
        }
    } else {
        next()
    }
})

export default router;
