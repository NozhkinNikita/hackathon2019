<template>
    <div>
        <md-dialog :md-active.sync="showDialog">
            <md-dialog-title>Add user</md-dialog-title>


            <md-dialog-content>

                <md-field>
                    <label>Login</label>
                    <md-input v-model="user.login"></md-input>
                </md-field>

                <md-field>
                    <label>Password</label>
                    <md-input type="password" v-model="user.pwd"></md-input>
                </md-field>

                <md-field>
                    <label>Name</label>
                    <md-input v-model="user.fio"></md-input>
                </md-field>

                <md-field>
                    <label for="movies">Roles</label>
                    <md-select v-model="user.roles" name="movies" id="movies" multiple>
                        <md-option value="SECUTITY_ADMIN">Админ безопасности</md-option>
                        <md-option value="USER">Пользователь</md-option>
                        <md-option value="NETWORK_ADMIN">Админ сети</md-option>
                    </md-select>
                </md-field>


                <md-checkbox class="md-success" v-model="user.enabled">Enabled</md-checkbox>


            </md-dialog-content>
            <md-dialog-actions>
                <md-button class="md-success" @click="add" :disabled="isDisabledAddButton()">Add</md-button>
            </md-dialog-actions>
        </md-dialog>

        <md-button class="md-success md-just-icon" @click="showDialog = true">
            <md-icon>add</md-icon>
        </md-button>
    </div>
</template>

<script>
    export default {
        name: 'user-add',
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
            user: {
                login: "",
                fio: "",
                roles: [],
                enabled: true,
                pwd: ""
            },
            locations: [],
        }),

        methods: {



            close() {
                this.showDialog = false;
            },

            isDisabledAddButton() {
                return !this.user.roles.length > 0
            },

            add() {


                console.log("add")
                this.$http.post(this.$hostname + '/api/security/users/',

                    this.user
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
                        this.user = {
                            login: "",
                            fio: "",
                            roles: [],
                            enabled: true,
                            pwd: ""
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
                        this.locations = response.locations;
                    })
                    .catch(function (error) {
                        console.log("auth fuck");
                        console.log(error);
                        console.error(error.response);
                    });

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