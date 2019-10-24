<template>
    <div>
        <md-table v-model="users" :table-header-color="tableHeaderColor">
            <md-table-row slot="md-table-row" slot-scope="{ item }">
                <md-table-cell md-label="Id">{{ item.id }}</md-table-cell>
                <md-table-cell md-label="Login">{{ item.login }}</md-table-cell>
                <md-table-cell md-label="Name">{{ item.fio }}</md-table-cell>
                <md-table-cell md-label="Roles">{{ item.roles }}</md-table-cell>
                <md-table-cell md-label="Enabled">{{ item.enabled }}</md-table-cell>
                <md-table-cell md-label="Action">
                    <user-crud :user-id="item.id"></user-crud>
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
            };
        },


        methods: {

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

        },
        mounted() {
            this.getUsers();
        },
    };
</script>
