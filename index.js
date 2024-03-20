const express = require('express');
const app = express();
const path = require('path');

app.use(express.static(path.join(__dirname, 'frontend')));

const PORT = process.env.PORT || 8001;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
