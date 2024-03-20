// app.js

new Vue({
    el: '.container',
    data: {
      blockchain: [], // 初始化区块链数据
      chartData: { labels: [], values: [] } // 初始化图表数据
    },
    mounted() {
      // 在组件挂载完成后立即刷新区块链数据和图表数据
      this.refreshBlockchain();
    },
    methods: {
      // 定义刷新区块链数据的方法
      refreshBlockchain() {
        fetch('http://127.0.0.1:8000/blockchain')
          .then(response => response.json())
          .then(data => {
            this.blockchain = data; // 将获取的区块链数据赋值给组件中的blockchain属性
            this.updateChartData(); // 更新图表数据
          })
          .catch(error => {
            console.error('Error fetching blockchain:', error);
          });
      },
      // 定义挖矿的方法
      mineBlock() {
        fetch('http://127.0.0.1:8000/mine', {
          method: 'POST' // 发送POST请求以触发挖矿
        })
          .then(response => response.json())
          .then(newBlock => {
            console.log('New block mined:', newBlock);
            this.refreshBlockchain(); // 挖矿完成后刷新区块链数据
          })
          .catch(error => {
            console.error('Error mining block:', error);
          });
      },
      // 更新图表数据的方法
      updateChartData() {
        // 清空原有的图表数据
        this.chartData.labels = [];
        this.chartData.values = [];
  
        // 将区块链数据转换为图表所需的数据格式
        this.blockchain.forEach(block => {
          this.chartData.labels.push(block.index);
          this.chartData.values.push(block.data.length);
        });
  
        // 使用Chart.js更新图表
        this.updateChart();
      },
      // 使用Chart.js更新图表的方法
      updateChart() {
        const ctx = document.getElementById('blockchain-chart').getContext('2d');
        new Chart(ctx, {
          type: 'line',
          data: {
            labels: this.chartData.labels,
            datasets: [{
              label: 'Block Data Size',
              data: this.chartData.values,
              backgroundColor: 'rgba(54, 162, 235, 0.2)',
              borderColor: 'rgba(54, 162, 235, 1)',
              borderWidth: 1
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
              yAxes: [{
                ticks: {
                  beginAtZero: true
                }
              }]
            }
          }
        });
      }
    }
  });
  