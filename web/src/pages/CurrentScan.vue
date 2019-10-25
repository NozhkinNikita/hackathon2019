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

                <!--                {{points.points}}<br><br><br>-->

                <nav-tabs-card>
                    <template slot="content">
                        <span class="md-nav-tabs-title">Scan:</span>
                        <md-tabs class="md-success" md-alignment="left">
                            <md-tab id="tab-home" md-label="Points" md-icon="bug_report">

                                <points-table :points="points" table-header-color="green"></points-table>


                            </md-tab>

                            <md-tab id="tab-pages" md-label="Graph" md-icon="code">

                                <template v-if="isShow()">
                                    <chart-card
                                            :chart-data="emailsSubscriptionChart.data"
                                            :chart-options="emailsSubscriptionChart.options"
                                            :chart-responsive-options="emailsSubscriptionChart.responsiveOptions"
                                            :chart-type="'Bar'"
                                            data-background-color="black"
                                    >

                                    </chart-card>
                                </template>

                            </md-tab>

                            <md-tab id="tab-posts" md-label="device" md-icon="cloud">


                                <p><b>model: </b> &nbsp; {{ points.device.model }}</p>
                                <p><b>Version os: </b> &nbsp; {{ points.device.osVersion }}</p>
                                <p><b>Manufacturer: </b>&nbsp;{{ points.device.manufacturer }}</p>
                                <p><b>Brand: </b>&nbsp;{{ points.device.brand }}></p>
                                <p><b>Device id: </b>&nbsp;{{ points.device.UNICORN_deviceId }}</p>
                                <p><b>Device: </b>&nbsp;{{ points.device.device }}</p>
                                <p><b>Mac: </b>&nbsp;{{ points.device.mac }}</p>
                                <p><b>IpV4: </b>&nbsp;{{ points.device.ipV4 }}</p>
                                <p><b>Release: </b>&nbsp;{{ points.device.release }}</p>
                                <p><b>Product: </b>&nbsp;{{ points.device.product }}</p>
                                <p><b>Serial: </b>&nbsp;{{ points.device.serial }}</p>
                                <p><b>User: </b>&nbsp;{{ points.device.user }}</p>
                                <p><b>Host: </b>&nbsp;{{ points.device.host }}</p>

                                <b>Model: </b>&nbsp;{{ points.device.model }}<br/>

                            </md-tab>
                        </md-tabs>
                    </template>
                </nav-tabs-card>


            </div>


        </div>
    </div>
</template>

<script>

    import {
        ScansTable, OrderedTable, LocationTable, LocationAdd, PointsTable, NavTabsCard,
        NavTabsTable, ChartCard
    } from "@/components";
    import {EditProfileForm, UserCard} from "@/pages";

    export default {
        components: {
            EditProfileForm,
            UserCard,
            OrderedTable,
            LocationTable,
            LocationAdd,
            ScansTable,
            PointsTable,
            NavTabsCard,
            NavTabsTable,
            ChartCard
        },

        methods: {

            isShow() {
                return this.emailsSubscriptionChart.data.series.length > 0 &&
                    this.emailsSubscriptionChart.data.labels.length > 0;
            },
            getDataForGraph() {

                // return [[100,20,40,50]];

                let graphData = {};
                let temp = [];
                console.log(this.points.points);
                for (var point in this.points.points) {

                    console.log("ppppppppp");
                    console.log(point);
                    for (var    routers in this.points.points[point].routerDatas) {
                        console.log("iiiiiii");
                        console.log(routers);

                        let name= this.points.points[point].routerDatas[routers].id;
                        let rssi = this.points.points[point].routerDatas[routers].rssi;
                        if (Object.keys(graphData).includes(name)) {
                            graphData[name].push(rssi);
                        } else {
                            graphData[name] = [];
                            graphData[name].push(rssi);

                        }
                    }


                }
                Object.keys(graphData).forEach(key => temp.push(graphData[key]));
                console.log("zyzyzy");
                console.log(graphData);
                console.log(temp);
                   return temp;


            },
            getLabelsForGraph() {

                // return [[100,20,40,50]];

                console.log("GGgggggggggg");
                console.log(this.points.points.map(value => value.name));
                return this.points.points.map(value => value.name);


            },

            getPoints(id) {
                // your code to login user
                // this is only for example of loading

                // this.loading = true;
                // setTimeout(() => {
                //   this.loading = false;
                // }, 5000);


                this.$http.get(this.$hostname + '/api/user/scans/' + id, {
                    headers: {
                        Authorization: "Kfmn " + localStorage.getItem("jwt")
                    }
                })


                    .then(response => {
                        console.log("ppppppp");
                        console.log(response);
                        this.points = response.data;

                        // this.emailsSubscriptionChart.data.series = this.points.points;

                        // this.emailsSubscriptionChart.data.series = [[100, 20, 40, 50]];

                        this.emailsSubscriptionChart.data.series = this.getDataForGraph();
                        // this.emailsSubscriptionChart.data.series = [[3.14, 10, 5]];


                        this.emailsSubscriptionChart.data.labels = this.getLabelsForGraph();
                    })
                    .catch(function (error) {
                        console.log("auth fuck");
                        console.log(error);
                        console.error(error.response);
                    });

            },
            // toggleHeader() {
            //     this.getScans();
            // }

        },
        mounted() {
            this.getPoints(this.$route.params.id);


        },

        data() {


            return {
                points: [
                    {}
                ],


                emailsSubscriptionChart: {
                    data: {
                        labels: [],
                        // series: [[542, 443, 320, 780, 553, 453, 326, 434, 568, 610, 756, 895],
                        //     [442, 643, 370, 480, 523, 453, 226, 444, 678, 612, 796, 775],
                        //     [532, 243, 120, 790, 533, 423, 316, 404, 368, 810, 256, 195],
                        //     [443, 443, 120, 790, 443, 423, 316, 404, 368, 810, 256, 195],
                        //     [532, 243, 120, 790, 533, 423, 316, 404, 368, 810, 256, 195],
                        //     [532, 243, 120, 790, 533, 423, 316, 404, 368, 810, 256, 195],
                        //     [532, 243, 120, 790, 533, 423, 316, 404, 368, 810, 256, 195],
                        //     [532, 243, 120, 790, 533, 423, 316, 404, 368, 810, 256, 195],
                        //
                        // ]


                        series: [],


                    },
                    options: {
                        axisX: {
                            showGrid: true
                        },
                        low: 0,
                        high: 20,
                        chartPadding: {
                            top: 0,
                            right: 5,
                            bottom: 0,
                            left: 0
                        }
                    },
                    responsiveOptions: [
                        [
                            "screen and (max-width: 640px)",
                            {
                                seriesBarDistance: 5,
                                axisX: {
                                    labelInterpolationFnc: function (value) {
                                        return value[0];
                                    }
                                }
                            }
                        ]
                    ]
                }

            }
        }
    };
</script>

<!--<style src="/node_modules/c3/c3.css"></style>-->
