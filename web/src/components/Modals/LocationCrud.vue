<template>
    <div>


        <md-dialog :md-active.sync="showDialog">
            <md-dialog-title>Edit {{location.login}}</md-dialog-title>


            <md-dialog-content>
{{currentUsers}}

                <md-field>
                    <label>Name</label>
                    <md-input v-model="location.name"></md-input>
                </md-field>


                <md-field>
                    <label for="locations">Locations</label>
                    <md-select v-model="currentUsers" name="locations" id="locations" multiple>
                        <md-option v-for="user in users" :value="user.id">{{user.login}}</md-option>
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
        name: 'location-crud',
        props: {
            locationId: {
                type: String,
                default: ""
            },
            users: {
                type: [],
                default: ""
            },

        },
        data: () => ({
            boolean: false,
            showDialog: false,
            location: {},
            currentUsers: []
        }),

        methods: {

            close() {
                this.showDialog = false;
            },

            save() {


                console.log("save")
                this.$http.put(this.$hostname + '/api/security/locations/',
                    {
                        location: this.location,
                         userIds: this.currentUsers
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


            getLocation(id) {
                // your code to login user
                // this is only for example of loading

                // this.loading = true;
                // setTimeout(() => {
                //   this.loading = false;
                // }, 5000);


                this.$http.get(this.$hostname + '/api/security/locations/' + id, {
                    headers: {
                        Authorization: "Kfmn " + localStorage.getItem("jwt")
                    }
                })


                    .then(response => {
                        console.log("users get suc");
                        console.log(response);
                        this.location = response.data.location;
                        this.currentUsers = response.data.userIds;

                    })
                    .catch(function (error) {
                        console.log("auth fuck");
                        console.log(error);
                        console.error(error.response);
                    });

            },

            open() {
                this.showDialog = true;
                this.getLocation(this.locationId)
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