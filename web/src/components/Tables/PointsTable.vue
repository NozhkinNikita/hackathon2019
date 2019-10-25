<template>
    <div>
        <!--        v-model="users"-->
        <!--        :users.sync="users"-->
        <md-table
                v-model="points.points"


                :table-header-color="tableHeaderColor">
            <md-table-row slot="md-table-row" slot-scope="{ item }">
                <!--                <md-table-cell md-label="Id">{{ item.id }}</md-table-cell>-->
                <md-table-cell md-label="Name">{{ item.name }}</md-table-cell>
                <md-table-cell md-label="Begin">{{ item.begin }}</md-table-cell>
                <md-table-cell md-label="End">{{ item.end }}</md-table-cell>
<!--                <md-table-cell md-label="IsRepeat">{{ item.isRepeat }}</md-table-cell>-->


            </md-table-row>



        </md-table>
    </div>
</template>

<script>


    import LocationCrud from "../Modals/LocationCrud";

    export default {
        name: "points-table",
        components: {LocationCrud},
        props: {
            tableHeaderColor: {
                type: String,
                default: ""
            },
            points: {
                type: Array,
                default: [],
            }
        },
        data() {
            return {
                selected: [],
                users: [],
            };
        },


        methods: {


            getLocation(location) {
                return location.location.name;
            },

            open(id) {

                this.$router.push("/scans/" + id);

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


                this.$http.get(this.$hostname + '/api/security/locations/', {
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
                this.getLocations();
            }

        },
        mounted() {
            // this.getUsers();
        },
    };
</script>
