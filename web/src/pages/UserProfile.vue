<!--<template>-->
<!--  <div class="content">-->
<!--    <div class="md-layout">-->
<!--      <div class="md-layout-item md-medium-size-100 md-size-66">-->
<!--        <edit-profile-form data-background-color="green"> </edit-profile-form>-->
<!--      </div>-->
<!--      <div class="md-layout-item md-medium-size-100 md-size-33">-->
<!--        <user-card> </user-card>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<template>
    <div class="content">
        <div class="md-layout">
            <div
                    class="md-layout-item md-medium-size-100 md-xsmall-size-100 md-size-100"
            >

                <md-card>
                    <md-card-header data-background-color="green">
                        <h4 class="title">Users</h4>
                        <!--                        <p class="category">Here is a subtitle for this table</p>-->
                    </md-card-header>
                    <md-card-content>
                        <simple-table :users="users" table-header-color="green"></simple-table>

                        <user-add v-on:toggle="toggleHeader()"></user-add>
                    </md-card-content>
                </md-card>
            </div>

        </div>
    </div>
</template>

<script>

    import {SimpleTable, OrderedTable, UserCrud, UserAdd} from "@/components";
    import {EditProfileForm, UserCard} from "@/pages";

    export default {
        components: {
            EditProfileForm,
            UserCard,
            OrderedTable,
            SimpleTable,
            UserCrud,
            UserAdd
        },

        methods: {

            addUser() {


            },

            getUsers() {
                // your code to login user
                // this is only for example of loading

                // this.loading = true;
                // setTimeout(() => {
                //   this.loading = false;
                // }, 5000);


                this.$http.get(this.$hostname + '/api/security/users/', {
                    headers: {
                        Authorization: "Kfmn " + localStorage.getItem("jwt")
                    }
                })


                    .then(response => {
                        console.log("users get suc");
                        console.log(response)
                        this.users = response.data
                    })
                    .catch(function (error) {
                        console.log("auth fuck");
                        console.log(error);
                        console.error(error.response);
                    });

            },
            toggleHeader(){
                this.getUsers();
            }
        },
        mounted() {
            this.getUsers();
        },

        data() {
            return {
                users: [
                    {}
                ]
            }
        }
    };
</script>
