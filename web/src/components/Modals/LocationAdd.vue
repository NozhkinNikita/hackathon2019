<template>
    <div>
        <md-dialog :md-active.sync="showDialog">
            <md-dialog-title>Add location</md-dialog-title>


            <md-dialog-content>


                <md-field>
                    <label>Location</label>
                    <md-input v-model="location.name">Name</md-input>
                </md-field>

            </md-dialog-content>
            <md-dialog-actions>
                <md-button class="md-success" @click="add">Add</md-button>
            </md-dialog-actions>
        </md-dialog>

        <md-button class="md-success md-just-icon" @click="showDialog = true">
            <md-icon>add</md-icon>
        </md-button>
    </div>
</template>

<script>
    export default {
        name: 'location-add',
        props: {
            userId: {
                type: String,
                default: ""
            },
        },
        data: () => ({
            selectedMovies: [],
            boolean: false,
            showDialog: false,
            location: {
                name: "",
            },
            locations: [],
        }),

        methods: {


            close() {
                this.showDialog = false;
            },

            add() {

                console.log("add")
                this.$http.post(this.$hostname + '/api/security/locations/',

                    this.location
                    ,
                    {
                        headers: {
                            Authorization: "Kfmn " + localStorage.getItem("jwt")
                        }
                    }
                )


                    .then(response => {
                        console.log("userssssssssssssssssssssssssssss");
                        console.log(response);
                        this.location = {
                            name: "",
                        };
                        this.$emit('toggle');

                    })
                    .catch(function (error) {
                        console.log("auth fuckkkkkkkkkkkkkkkkkkkkkkkk");
                        console.log(error);
                        console.error(error.response);
                    });


                this.showDialog = false;
            },


        },
        // mounted() {
        //     this.getUser(this.userId);
        // },


    }
</script>

<style lang="scss" scoped>
    .md-dialog {
        max-width: 768px;
    }

    .md-checkbox.md-checked .md-checkbox-container:after {
        border-color: green !important;
    }
</style>