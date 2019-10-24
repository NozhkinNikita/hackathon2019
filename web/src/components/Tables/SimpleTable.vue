<template>
    <div>
        <!--        v-model="users"-->
        <!--        :users.sync="users"-->
        <md-table
                v-model="users"


                :table-header-color="tableHeaderColor">
            <md-table-row slot="md-table-row" slot-scope="{ item }">
                <!--                <md-table-cell md-label="Id">{{ item.id }}</md-table-cell>-->
                <md-table-cell md-label="Login">{{ item.login }}</md-table-cell>
                <md-table-cell md-label="Name">{{ item.fio }}</md-table-cell>
                <md-table-cell md-label="Roles">{{ printRoles(item.roles)}}</md-table-cell>
                <md-table-cell md-label="Enabled">{{ item.enabled }}</md-table-cell>
                <md-table-cell md-label="Action">
                    <user-crud :user-id="item.id" :locations="locations" v-on:toggle="toggleHeader()"></user-crud>
                    <md-button class="md-danger" @click="deleteUser(item.id)">Delete</md-button>

                </md-table-cell>
            </md-table-row>
        </md-table>
    </div>
</template>

<script>


    import UserCrud from "../Modals/UserCrud";

    export default {
        name: "simple-table",
        components: {UserCrud},
        props: {
            tableHeaderColor: {
                type: String,
                default: ""
            },
            users: {
                type: Array,
                default: [],
            }
        },
        data() {
            return {
                selected: [],
                locations: [],
            };
        },


        methods: {

            printRoles(roles) {

                return roles.map(role => this.mapUserRole(role)).join(", ");
            },

            mapUserRole(role) {
                var roles = [];
                roles["USER"] = "пользователь";
                roles["SECUTITY_ADMIN"] = "админ безопасности";
                roles["NETWORK_ADMIN"] = "админ сети";
                return roles[role];
            },

            deleteUser(userId) {

                console.log("delete");
                this.$http.delete(this.$hostname + '/api/security/users/' + userId,

                    {
                        headers: {
                            Authorization: "Kfmn " + localStorage.getItem("jwt")
                        }
                    }
                )


                    .then(response => {
                        console.log("userssssssssssssssssssssssssssss");
                        console.log(response);
                        this.getUsers();

                    })
                    .catch(function (error) {
                        console.log("auth fuckkkkkkkkkkkkkkkkkkkkkkkk");
                        console.log(error);
                        console.error(error.response);
                    });


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
                        console.log("get users333");
                        console.log(response)
                        this.users = response.data
                    })
                    .catch(function (error) {
                        console.log("auth fuck");
                        console.log(error);
                        console.error(error.response);
                    });

            },


            getLocations() {

                // your code to login user
                // this is only for example of loading

                // this.loading = true;
                // setTimeout(() => {
                //   this.loading = false;
                // }, 5000);


                this.$http.get(this.$hostname + '/api/security/location/', {
                    headers: {
                        Authorization: "Kfmn " + localStorage.getItem("jwt")
                    }
                })


                    .then(response => {
                        console.log("get locations");
                        console.log(response);
                        this.locations = response.data
                        console.log("===========================")
                        console.log(this.locations)
                    })
                    .catch(function (error) {
                        console.log("auth fuck");
                        console.log(error);
                        console.error(error.response);
                    });

            },

            toggleHeader() {
                this.getUsers();
            }

        },
        mounted() {
            this.getLocations();
        },
    };
</script>
