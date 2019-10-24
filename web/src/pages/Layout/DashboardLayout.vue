<template>
  <div class="wrapper" :class="{ 'nav-open': $sidebar.showSidebar }">
    <notifications></notifications>

    <side-bar>
      <mobile-menu slot="content"></mobile-menu>
      <sidebar-link to="/users">
        <md-icon>person</md-icon>
        <p>Users</p>
      </sidebar-link>
      <sidebar-link to="/locations">
        <md-icon>my_location</md-icon>
        <p>Locations</p>
      </sidebar-link>
      <sidebar-link to="/routers">
        <md-icon class="router-icon">routers</md-icon>
        <p>Routers</p>
      </sidebar-link>
    </side-bar>

    <div class="main-panel">
      <top-navbar></top-navbar>

      <dashboard-content> </dashboard-content>

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
  }
};
</script>

<style lang="scss">
  .router-icon {
    padding-left: 23px;
  }
</style>
