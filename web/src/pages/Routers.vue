<template>
    <div class="content">
        <div class="md-layout">


            <div
                    class="md-layout-item md-medium-size-100 md-xsmall-size-100 md-size-100"
            >
                <md-card class="md-card-plain">
                    <md-card-header data-background-color="green">
                        <h4 class="title">Routers</h4>
                    </md-card-header>
                    <md-card-content>
                        <ordered-table :routers="routers"></ordered-table>
                    </md-card-content>
                </md-card>
            </div>
        </div>
    </div>
</template>

<!--<script>-->

<!--    import {SimpleTable, OrderedTable, UserCrud, UserAdd} from "@/components";-->
<!--    import {EditProfileForm, UserCard} from "@/pages";-->

<!--    export default {-->
<!--        components: {-->
<!--            EditProfileForm,-->
<!--            RouterCard,-->
<!--            OrderedTable,-->
<!--            SimpleTable,-->
<!--            RouterCrud,-->
<!--            RouterAdd-->
<!--        },-->

<!--        methods: {-->

<!--            addRouter() {-->


<!--            },-->

<!--            getRouters() {-->
<!--                // your code to login user-->
<!--                // this is only for example of loading-->

<!--                // this.loading = true;-->
<!--                // setTimeout(() => {-->
<!--                //   this.loading = false;-->
<!--                // }, 5000);-->


<!--                this.$http.get(this.$hostname + '/api/admin/routers/', {-->
<!--                    headers: {-->
<!--                        Authorization: "Kfmn " + localStorage.getItem("jwt")-->
<!--                    }-->
<!--                })-->


<!--                    .then(response => {-->
<!--                        console.log("users get suc");-->
<!--                        console.log(response)-->
<!--                        this.users = response.data-->
<!--                    })-->
<!--                    .catch(function (error) {-->
<!--                        console.log("auth fuck");-->
<!--                        console.log(error);-->
<!--                        console.error(error.response);-->
<!--                    });-->

<!--            },-->

<!--        },-->
<!--        mounted() {-->
<!--            this.getRouters();-->
<!--        },-->

<!--        data() {-->
<!--            return {-->
<!--                routers: [-->
<!--                    {}-->
<!--                ]-->
<!--            }-->
<!--        }-->
<!--    };-->
<!--</script>-->