let currentBatch = 0;
let currentPage = 0;

// 파일 업로드를 위한 함수
document.getElementById('uploadForm').addEventListener('submit', function(event) {
    event.preventDefault();
    var formData = new FormData();
    var fileInput = document.getElementById('file');
    formData.append('file', fileInput.files[0]);

    fetch('/api/excel/upload', {
        method: 'POST',
        body: formData
    })
    .then(response => response.text())
    .then(data => {
        alert(data);
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error uploading Excel file.');
    });
});

// 검색을 위한 함수
document.getElementById('searchForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const query = document.getElementById('query').value;
    currentBatch = 0;
    currentPage = 0;
    fetchData(query, 0);
});

function fetchData(query, page) {
    console.log(`Fetching data for query: ${query}, page: ${page}`);

    fetch(`/api/excel/search?query=${query}&page=${page}&size=5`)
        .then(response => {
            console.log(`HTTP Status: ${response.status}`);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const resultsDiv = document.getElementById('results');
            resultsDiv.innerHTML = createTable(data.content);

            const paginationDiv = document.getElementById('pagination');
            paginationDiv.innerHTML = createPagination(data.totalPages, query, page);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error fetching data');
        });
}

function createTable(data) {
    if (data.length === 0) {
        return '<p>No results found</p>';
    }

    let table = '<table><tr><th>First Level</th><th>Second Level</th><th>Third Level</th><th>Grid X</th><th>Grid Y</th></tr>';
    data.forEach(item => {
        table += `<tr>
            <td>${item.firstLevel}</td>
            <td>${item.secondLevel}</td>
            <td>${item.thirdLevel}</td>
            <td>${item.gridX}</td>
            <td>${item.gridY}</td>
        </tr>`;
    });
    table += '</table>';
    return table;
}

function createPagination(totalPages, query, currentPage) {
    let pagination = '';
    const pagesPerBatch = 10;
    const totalBatches = Math.ceil(totalPages / pagesPerBatch);
    const currentBatch = Math.floor(currentPage / pagesPerBatch);

    if (totalPages <= 1) {
        return pagination;
    }

    const currentBatchStart = currentBatch * pagesPerBatch;
    const currentBatchEnd = Math.min(currentBatchStart + pagesPerBatch, totalPages);

    if (currentBatch > 0) {
        pagination += `<a href="#" class="nav-btn" onclick="changeBatch(${currentBatch - 1}, '${query}')">&laquo;</a>`;
    }

    // '<' 버튼을 클릭했을 때 이전 배치의 마지막 페이지로 이동하는 기능 추가
    if (currentPage === currentBatchStart && currentBatch > 0) {
        pagination += `<a href="#" class="nav-btn" onclick="fetchData('${query}', ${currentBatchStart - 1})">&lt;&middot;</a>`;
    } else if (currentPage === currentBatchStart && currentBatch === 0) {
        pagination += `<a class="nav-btn disabled">&lt;</a>`;
    } else {
        pagination += `<a href="#" class="nav-btn" onclick="fetchData('${query}', ${currentPage - 1})">&lt;&middot;</a>`;
    }

    for (let i = currentBatchStart; i < currentBatchEnd; i++) {
        pagination += `<a href="#" onclick="fetchData('${query}', ${i})" ${i === currentPage ? 'class="active"' : ''}>${i + 1}</a>`;
    }

    if (currentPage < totalPages - 1) {
        pagination += `<a href="#" class="nav-btn" onclick="fetchData('${query}', ${currentPage + 1})">&middot;&gt;</a>`;
    }

    // '>' 버튼을 클릭했을 때 다음 배치의 첫 페이지로 이동하는 기능 추가
    if (currentBatch < totalBatches - 1) {
        pagination += `<a href="#" class="nav-btn" onclick="changeBatch(${currentBatch + 1}, '${query}')">&raquo;</a>`;
    }
    return pagination;
}

// 현재 배치를 변경하고 해당 배치의 첫 페이지를 표시
function changeBatch(batch, query) {
    currentBatch = batch;
    const newPage = batch * 10;
    fetchData(query, newPage);
}
