// 语言切换按钮点击事件处理
document.getElementById('englishButton').addEventListener('click', function() {
    console.log('Switch to English');
    document.getElementById('blockHeader').innerText = "Latest Block";
    document.getElementById('blockNumberLabel').innerText = "Block Number:";
    document.getElementById('timestampLabel').innerText = "Timestamp:";
    document.getElementById('nonceLabel').innerText = "Nonce:";
    document.getElementById('previousHashLabel').innerText = "Previous Hash:";
    document.getElementById('hashLabel').innerText = "Hash:";
    document.getElementById('transactionHeader').innerText = "Transaction List";
    // 在这里添加切换到英文的逻辑
});

document.getElementById('chineseButton').addEventListener('click', function() {
    console.log('Switch to Chinese');
    document.getElementById('blockHeader').innerText = "最新区块";
    document.getElementById('blockNumberLabel').innerText = "区块编号：";
    document.getElementById('timestampLabel').innerText = "时间戳：";
    document.getElementById('nonceLabel').innerText = "Nonce：";
    document.getElementById('previousHashLabel').innerText = "前一个哈希：";
    document.getElementById('hashLabel').innerText = "哈希：";
    document.getElementById('transactionHeader').innerText = "交易列表";
    // 在这里添加切换到中文的逻辑
});

// 挖矿按钮点击事件处理
document.getElementById('mineButton').addEventListener('click', function() {
    console.log('Mine block button clicked');
    // 模拟挖矿过程...
    // 更新区块链信息
    mineBlock();
});

// 添加交易按钮点击事件处理
document.getElementById('addTransactionButton').addEventListener('click', function() {
    console.log('Add transaction button clicked');
    // 模拟添加交易...
    // 更新交易列表
    addTransaction();
});

// 模拟挖矿函数
function mineBlock() {
    // 模拟挖矿过程...
    // 更新最新区块的信息
    document.getElementById('blockNumber').innerText = "2";
    document.getElementById('timestamp').innerText = "2024-03-21 11:00:00";
    document.getElementById('nonce').innerText = "54321";
    document.getElementById('previousHash').innerText = "0000abcd12345678"; // 更新为完整的前一个哈希值
    document.getElementById('hash').innerText = "abcd1234efgh5678"; // 更新为完整的哈希值
}

// 模拟添加交易函数
function addTransaction() {
    // 模拟添加交易过程...
    // 更新交易列表
    var transactionList = document.getElementById('transactionList');
    var newTransaction = document.createElement('li');
    newTransaction.textContent = "Transaction 3: David -> Emily ($75)";
    transactionList.appendChild(newTransaction);
}

// 柱状图表显示区块状态
var ctx = document.getElementById('blockchainChart').getContext('2d');
var blockchainChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: ['Block Number', 'Timestamp', 'Nonce'],
        datasets: [{
            label: 'Block Status',
            data: [1, 12345, 54321], // 示例数据
            backgroundColor: [
                'rgba(255, 99, 132, 0.2)', // 红色
                'rgba(54, 162, 235, 0.2)', // 蓝色
                'rgba(255, 206, 86, 0.2)' // 黄色
            ],
            borderColor: [
                'rgba(255, 99, 132, 1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)'
            ],
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});
