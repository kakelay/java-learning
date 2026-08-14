<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta charset="UTF-8">
    <link href="webjars/bootstrap/5.1.3/css/bootstrap.min.css" rel="stylesheet">
    <title>Add Todo</title>
    <style>
        body {
            background-color: #f5f5f5;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
        }
        .todo-card {
            max-width: 520px;
            margin: 3rem auto;
            background: #ffffff;
            border: 1px solid #e5e5e5;
            border-radius: 12px;
            overflow: hidden;
        }
        .todo-header {
            padding: 1.25rem 1.5rem;
            border-bottom: 1px solid #f0f0f0;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .todo-icon {
            width: 34px;
            height: 34px;
            background: #e6f1fb;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .todo-icon svg {
            width: 16px;
            height: 16px;
            stroke: #185fa5;
            fill: none;
            stroke-width: 2;
            stroke-linecap: round;
            stroke-linejoin: round;
        }
        .todo-title {
            font-size: 15px;
            font-weight: 500;
            color: #1a1a1a;
            margin: 0;
        }
        .todo-subtitle {
            font-size: 12px;
            color: #888;
            margin: 2px 0 0;
        }
        .badge-new {
            font-size: 11px;
            padding: 2px 8px;
            background: #eaf3de;
            color: #3b6d11;
            border-radius: 6px;
            font-weight: 500;
            margin-left: 8px;
            vertical-align: middle;
        }
        .todo-body {
            padding: 1.5rem;
            display: flex;
            flex-direction: column;
            gap: 1rem;
        }
        .field-group {
            display: flex;
            flex-direction: column;
            gap: 5px;
        }
        .field-group label {
            font-size: 13px;
            font-weight: 500;
            color: #555;
        }
        .field-group textarea,
        .field-group input[type="date"],
        .field-group select {
            width: 100%;
            padding: 8px 12px;
            font-size: 14px;
            background: #f9f9f9;
            border: 1px solid #ddd;
            border-radius: 8px;
            color: #1a1a1a;
            outline: none;
            box-sizing: border-box;
            transition: border-color 0.15s, box-shadow 0.15s;
            appearance: none;
            -webkit-appearance: none;
        }
        .field-group textarea:focus,
        .field-group input[type="date"]:focus,
        .field-group select:focus {
            border-color: #378add;
            box-shadow: 0 0 0 3px rgba(55,138,221,0.1);
            background: #fff;
        }
        .field-group textarea {
            resize: vertical;
            min-height: 80px;
            line-height: 1.5;
            appearance: auto;
            -webkit-appearance: auto;
        }

        /* ── Custom select wrapper ── */
        .select-wrapper {
            position: relative;
        }
        .select-wrapper select {
            padding-right: 36px;
            cursor: pointer;
        }
        .select-wrapper::after {
            content: '';
            position: absolute;
            right: 12px;
            top: 50%;
            transform: translateY(-50%);
            width: 0;
            height: 0;
            border-left: 4px solid transparent;
            border-right: 4px solid transparent;
            border-top: 5px solid #888;
            pointer-events: none;
        }

        .hint {
            font-size: 12px;
            color: #aaa;
            margin: 0;
        }
        .char-count {
            font-size: 12px;
            color: #aaa;
            text-align: right;
        }
        .row-2 {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 12px;
        }
        .toggle-row {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 10px 12px;
            background: #f9f9f9;
            border-radius: 8px;
            border: 1px solid #ebebeb;
            transition: background 0.2s, border-color 0.2s;
        }
        .toggle-row.is-done {
            background: #eaf3de;
            border-color: #c0dd97;
        }
        .toggle-label {
            font-size: 14px;
            color: #333;
            transition: color 0.2s;
        }
        .toggle-row.is-done .toggle-label {
            color: #3b6d11;
            font-weight: 500;
        }

        /* ── Toggle ── */
        .toggle {
            position: relative;
            width: 40px;
            height: 22px;
            cursor: pointer;
            display: block;
            flex-shrink: 0;
        }
        .toggle input[type="checkbox"] {
            opacity: 0;
            width: 0;
            height: 0;
            position: absolute;
            margin: 0;
        }
        .toggle-track {
            position: absolute;
            inset: 0;
            background: #d1d1d1;      /* OFF: light gray */
            border-radius: 22px;
            transition: background 0.25s;
            pointer-events: none;
        }
        .toggle input[type="checkbox"]:checked + .toggle-track {
            background: #378add;      /* ON: blue */
        }
        .toggle-thumb {
            position: absolute;
            top: 3px;
            left: 3px;
            width: 16px;
            height: 16px;
            background: #ffffff;
            border-radius: 50%;
            box-shadow: 0 1px 3px rgba(0,0,0,0.2);
            transition: transform 0.25s;
            pointer-events: none;
        }
        .toggle input[type="checkbox"]:checked ~ .toggle-thumb {
            transform: translateX(18px);
        }
        /* ── End toggle ── */

        .text-danger {
            font-size: 12px;
            color: #e24b4a;
        }
        .todo-footer {
            padding: 0 1.5rem 1.5rem;
            display: flex;
            gap: 10px;
        }
        .btn-save {
            flex: 1;
            padding: 9px 0;
            font-size: 14px;
            font-weight: 500;
            color: white;
            background: #378add;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            transition: background 0.15s;
        }
        .btn-save:hover { background: #185fa5; }
        .btn-cancel {
            padding: 9px 18px;
            font-size: 14px;
            font-weight: 500;
            color: #555;
            background: transparent;
            border: 1px solid #ddd;
            border-radius: 8px;
            cursor: pointer;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
        }
        .btn-cancel:hover {
            background: #f5f5f5;
            color: #333;
        }
    </style>
</head>
<body>

<div class="todo-card">

    <!-- Header -->
    <div class="todo-header">
        <div class="todo-icon">
            <svg viewBox="0 0 24 24">
                <path d="M9 11l3 3L22 4"/>
                <path d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11"/>
            </svg>
        </div>
        <div>
            <p class="todo-title">
                Add todo <span class="badge-new">New</span>
            </p>
            <p class="todo-subtitle">Fill in the details below to create a task</p>
        </div>
    </div>

    <!-- Form -->
    <form:form method="post" modelAttribute="todo">

        <div class="todo-body">

            <!-- Description -->
            <div class="field-group">
                <form:label path="description">Description</form:label>
                <form:textarea path="description" rows="3"
                               placeholder="What needs to be done?"
                               oninput="updateCount(this)" />
                <div style="display:flex; justify-content:space-between; align-items:center;">
                    <p class="hint">Be specific &mdash; clear tasks get done faster.</p>
                    <span class="char-count" id="char-count">0 / 200</span>
                </div>
                <form:errors path="description" cssClass="text-danger" />
            </div>

            <!-- Target Date & Priority -->
            <div class="row-2">
                <div class="field-group">
                    <form:label path="targetDate">Target date</form:label>
                    <form:input path="targetDate" type="date" />
                </div>
                <div class="field-group">
                    <label for="priority">Priority</label>
                    <div class="select-wrapper">
                        <select id="priority" name="priority">
                            <option value="">Select...</option>
                            <option value="LOW">Low</option>
                            <option value="MEDIUM" selected>Medium</option>
                            <option value="HIGH">High</option>
                        </select>
                    </div>
                </div>
            </div>

            <!-- Done Toggle -->
            <div class="toggle-row" id="toggle-row">
                <span class="toggle-label">Mark as done</span>
                <label class="toggle">
                    <form:checkbox path="done" id="doneCheckbox" />
                    <div class="toggle-track"></div>
                    <div class="toggle-thumb"></div>
                </label>
            </div>

        </div>

        <!-- Hidden Fields -->
        <form:input type="hidden" path="id" />

        <!-- Footer -->
        <div class="todo-footer">
            <a href="/list-todos" class="btn-cancel">Cancel</a>
            <button type="submit" class="btn-save">Save todo</button>
        </div>

    </form:form>
</div>

<script src="webjars/jquery/3.6.0/jquery.min.js"></script>
<script src="webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js"></script>
<script>
    function updateCount(el) {
        const max = 200;
        const len = el.value.length;
        const counter = document.getElementById('char-count');
        counter.textContent = len + ' / ' + max;
        counter.style.color = len > max * 0.9 ? '#ba7517' : '#aaa';
        if (len > max) el.value = el.value.slice(0, max);
    }

    // Toggle row green highlight when checked
    const checkbox = document.getElementById('doneCheckbox');
    const toggleRow = document.getElementById('toggle-row');

    function syncToggleRow() {
        if (checkbox.checked) {
            toggleRow.classList.add('is-done');
        } else {
            toggleRow.classList.remove('is-done');
        }
    }

    checkbox.addEventListener('change', syncToggleRow);
    syncToggleRow(); // run on load in case editing an existing todo
</script>

</body>
</html>