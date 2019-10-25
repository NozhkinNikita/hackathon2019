<template>
    <div class="wrapper" :class="{ 'nav-open': $sidebar.showSidebar }">
        <notifications></notifications>

        <side-bar>
            <mobile-menu slot="content"></mobile-menu>
            <sidebar-link v-if="rolesContains('USER') || rolesContains('NETWORK_ADMIN')" to="/scans">
                <md-icon>perm_scan_wifi</md-icon>
                <p>Scans</p>
            </sidebar-link>
            <sidebar-link v-if="rolesContains('SECUTITY_ADMIN')" to="/users">
                <md-icon>person</md-icon>
                <p>Users</p>
            </sidebar-link>
            <sidebar-link v-if="rolesContains('SECUTITY_ADMIN') || rolesContains('NETWORK_ADMIN')" to="/locations">
                <md-icon>my_location</md-icon>
                <p>Locations</p>
            </sidebar-link>
        </side-bar>

        <div class="main-panel">
            <top-navbar></top-navbar>

            <dashboard-content></dashboard-content>

            <content-footer v-if="!$route.meta.hideFooter"></content-footer>
        </div>
    </div>
</template>
<style lang="scss"></style>
<script>
    import TopNavbar from "./TopNavbar.vue";
    import ContentFooter from "./ContentFooter.vue";
    import DashboardContent from "./Content.vue";
    import MobileMenu from "@/pages/Layout/MobileMenu.vue";

    export default {
        components: {
            TopNavbar,
            DashboardContent,
            ContentFooter,
            MobileMenu
        },
        methods: {
            rolesContains(role) {
                return JSON.parse(localStorage.getItem('roles')).includes(role);

            }
        }
    };
</script>

<style lang="scss">
    .router-icon {
        padding-left: 23px;
    }
</style>
