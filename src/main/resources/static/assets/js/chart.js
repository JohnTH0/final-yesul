  /*-------- Vendor dashboard [ Chart ] ---------*/

  (function($) {
    "use strict";
    var growthChartOptions = {
        series: [{
            name: 'Orders',
            type: 'bar',
            data: [23, 12, 23, 22, 15, 42, 31, 27, 45, 28, 37, 45]
        }, {
            name: 'Revenue',
            type: 'line',
            data: [44.64, 55.48, 45.15, 67.62, 52.57, 44.38, 41.85, 41.44, 56.56, 27.84, 43.78, 52.57]
        }, {
            name: 'Expense',
            type: 'line',
            data: [30.55, 24.67, 36.85, 37.08, 42.85, 38.85, 46.64, 45.42, 49.89, 36.56, 38.49, 55.57]
        }],
        chart: {
            height: 365,
            type: 'line',
            stacked: false,
            foreColor: '#373d3f',
            sparkline: {
                enabled: !1
            },
            toolbar: {
                show: !1
            }
        },
        stroke: {
            width: [0, 4, 4],
            curve: 'smooth',
        },
        plotOptions: {
            bar: {
                columnWidth: 30,
                borderRadius: 10,
            }
        },
        colors: ['#fadfa2', '#89a6ff', '#54d3c2'],
        xaxis: {
            categories: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
            axisTicks: {
                show: !1
            },
            axisBorder: {
                show: !1
            }
        },
        legend: {
            show: !0,
            horizontalAlign: "center",
            offsetX: 0,
            offsetY: 2,
            markers: {
                width: 12,
                height: 12,
                radius: 4
            },
            itemMargin: {
                horizontal: 10,
                vertical: 0
            }
        },
        grid: {
            show: !1,
            xaxis: {
                lines: {
                    show: !1
                }
            },
            yaxis: {
                lines: {
                    show: !1
                }
            },
            padding: {
                top: 0,
                right: -2,
                bottom: 15,
                left: 10
            },
        },
        tooltip: {
            theme: "dark",
            shared: !0,
            y: [{
                formatter: function (e) {
                    return void 0 !== e ? e.toFixed(0) + "k" : e
                }
            }, {
                formatter: function (e) {
                    return void 0 !== e ? "$" + e.toFixed(2) + "k" : e
                }
            }, {
                formatter: function (e) {
                    return void 0 !== e ? "$" + e.toFixed(2) + "k" : e
                }
            }]
        },
        responsive: [{
            breakpoint: 480,
            options: {
                chart: {
                    height: 300,
                },
                yaxis: {
                    show: false,
                },
                plotOptions: {
                    bar: {
                        columnWidth: 40,
                        borderRadius: 5,
                    }
                },
            },
        }]
    };
    var growthChartId = document.querySelector("#growthChart");
    var growthChart = growthChartId && new ApexCharts(growthChartId, growthChartOptions);
    growthChart && growthChart.render();

})(jQuery);