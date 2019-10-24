<template>
    <div>


        <md-dialog :md-active.sync="showDialog">
            <md-dialog-title>Edit {{user.login}}</md-dialog-title>


            <md-dialog-content>

                <md-field>
                    <label>Login</label>
                    <md-input v-model="user.login"></md-input>
                </md-field>

                <md-field>
                    <label>Name</label>
                    <md-input v-model="user.fio"></md-input>
                </md-field>

                <md-checkbox class="md-success" v-model="user.enabled">Enabled</md-checkbox>

                <md-field>
                    <label for="locations">Locations</label>
                    <md-select v-model="currentLocations" name="locations" id="locations" multiple>
                        <md-option v-for="location in locations" :value="location.id">{{location.name}}</md-option>
                    </md-select>
                </md-field>

            </md-dialog-content>
            <md-dialog-actions>
                <md-button class="md-primary" @click="close">Close</md-button>
                <md-button class="md-success" @click="save">Save</md-button>
            </md-dialog-actions>
        </md-dialog>

        <md-button class="md-success md-raised" @click="open">Edit</md-button>
    </div>
</template>

<script>
    export default {
        name: 'user-crud',
        props: {
            userId: {
                type: String,
                default: ""
            },
            locations: {
                type: [],
                default: ""
            },

        },
        data: () => ({
            boolean: false,
            showDialog: false,
            user: {},
            currentLocations: {}
        }),

        methods: {

            close() {
                this.showDialog = false;
            },

            save() {


                console.log("save")
                this.$http.put(this.$hostname + '/api/security/users/',
                    {
                        user: this.user,
                        locationIds: this.currentLocations
                    },
                    {
                        headers: {
                            Authorization: "Kfmn " + localStorage.getItem("jwt")
                        }
                    }
                )


                    .then(response => {
                        console.log("userssssssssssssssssssssssssssss");
                        console.log(response);
                        this.$emit('toggle');

                    })
                    .catch(function (error) {
                        console.log("auth fuckkkkkkkkkkkkkkkkkkkkkkkk");
                        console.log(error);
                        console.error(error.response);
                    });


                this.showDialog = false;

            },

            deleteUser() {

                console.log("delete")
                this.$http.delete(this.$hostname + '/api/security/users/' + this.userId,

                    {
                        headers: {
                            Authorization: "Kfmn " + localStorage.getItem("jwt")
                        }
                    }
                )


                    .then(response => {
                        console.log("userssssssssssssssssssssssssssss");
                        console.log(response);
                        this.$emit('toggle');

                    })
                    .catch(function (error) {
                        console.log("auth fuckkkkkkkkkkkkkkkkkkkkkkkk");
                        console.log(error);
                        console.error(error.response);
                    });


                this.showDialog = false;
            },

            getUser(id) {
                // your code to login user
                // this is only for example of loading

                // this.loading = true;
                // setTimeout(() => {
                //   this.loading = false;
                // }, 5000);


                this.$http.get(this.$hostname + '/api/security/users/' + id, {
                    headers: {
                        Authorization: "Kfmn " + localStorage.getItem("jwt")
                    }
                })


                    .then(response => {
                        console.log("users get suc");
                        console.log(response);
                        this.user = response.data.user;
                        this.currentLocations = response.data.locationIds;

                    })
                    .catch(function (error) {
                        console.log("auth fuck");
                        console.log(error);
                        console.error(error.response);
                    });

            },

            open() {
                this.showDialog = true;
                this.getUser(this.userId)
            }

        },


    }
</script>

<style lang="scss">


    .md-dialog {
        max-width: 768px;
    }

    /* .md-checkbox.md-checked .md-checkbox-container:after {
         border-color: green !important;
     }
 */
    .md-list-item-text {
        position: relative !important;
    }
</style>