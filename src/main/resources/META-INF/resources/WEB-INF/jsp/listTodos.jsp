<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <link href="webjars/bootstrap/5.1.3/css/bootstrap.min.css" rel="stylesheet">
    <title>My Todos</title>
    <style>
        body {
            background-color: #f5f5f5;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
        }

        .todo-card {
            max-width: 860px;
            margin: 3rem auto;
            background: #ffffff;
            border: 1px solid #e5e5e5;
            border-radius: 12px;
            overflow: hidden;
        }

        /* ── Header ── */
        .todo-header {
            padding: 1.25rem 1.5rem;
            border-bottom: 1px solid #f0f0f0;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .todo-header-left {
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
        .welcome-badge {
            font-size: 12px;
            padding: 4px 12px;
            background: #f9f9f9;
            border: 1px solid #ebebeb;
            border-radius: 20px;
            color: #555;
        }
        .welcome-badge strong {
            color: #1a1a1a;
        }

        /* ── Body ── */
        .todo-body {
            padding: 1.5rem;
        }

        /* ── Table ── */
        .todo-table {
            width: 100%;
            border-collapse: collapse;
            font-size: 14px;
        }
        .todo-table thead tr {
            background: #f9f9f9;
            border-bottom: 1px solid #ebebeb;
        }
        .todo-table thead th {
            padding: 10px 14px;
            font-size: 12px;
            font-weight: 500;
            color: #888;
            text-align: left;
            white-space: nowrap;
        }
        .todo-table tbody tr {
            border-bottom: 1px solid #f5f5f5;
            transition: background 0.1s;
        }
        .todo-table tbody tr:last-child {
            border-bottom: none;
        }
        .todo-table tbody tr:hover {
            background: #fafafa;
        }
        .todo-table td {
            padding: 12px 14px;
            color: #333;
            vertical-align: middle;
        }
        .td-id {
            font-size: 12px;
            color: #aaa;
            font-family: monospace;
        }
        .td-desc {
            font-weight: 500;
            color: #1a1a1a;
            max-width: 240px;
        }
        .td-user {
            font-size: 13px;
            color: #555;
        }
        .td-date {
            font-size: 13px;
            color: #555;
            white-space: nowrap;
        }

        /* ── Badges ── */
        .badge-done {
            display: inline-flex;
            align-items: center;
            gap: 4px;
            font-size: 11px;
            padding: 3px 10px;
            border-radius: 20px;
            font-weight: 500;
        }
        .badge-done.yes {
            background: #eaf3de;
            color: #3b6d11;
        }
        .badge-done.no {
            background: #f1efe8;
            color: #5f5e5a;
        }
        .badge-dot {
            width: 6px;
            height: 6px;
            border-radius: 50%;
        }
        .badge-done.yes .badge-dot { background: #639922; }
        .badge-done.no  .badge-dot { background: #888780; }

        /* ── Action buttons ── */
        .btn-action {
            display: inline-flex;
            align-items: center;
            gap: 5px;
            padding: 5px 12px;
            font-size: 12px;
            font-weight: 500;
            border-radius: 6px;
            cursor: pointer;
            text-decoration: none;
            border: 1px solid transparent;
            transition: background 0.15s;
        }
        .btn-edit {
            background: #e6f1fb;
            color: #185fa5;
            border-color: #b5d4f4;
        }
        .btn-edit:hover { background: #b5d4f4; color: #185fa5; }
        .btn-delete {
            background: #fcebeb;
            color: #a32d2d;
            border-color: #f7c1c1;
        }
        .btn-delete:hover { background: #f7c1c1; color: #a32d2d; }
        .btn-action svg {
            width: 13px;
            height: 13px;
            stroke: currentColor;
            fill: none;
            stroke-width: 2;
            stroke-linecap: round;
            stroke-linejoin: round;
        }

        /* ── Empty state ── */
        .empty-state {
            text-align: center;
            padding: 3rem 1rem;
            color: #aaa;
        }
        .empty-state svg {
            width: 40px;
            height: 40px;
            stroke: #ccc;
            fill: none;
            stroke-width: 1.5;
            margin-bottom: 12px;
        }
        .empty-state p {
            font-size: 14px;
            margin: 0 0 16px;
        }

        /* ── Footer ── */
        .todo-footer {
            padding: 1rem 1.5rem;
            border-top: 1px solid #f0f0f0;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .todo-count {
            font-size: 12px;
            color: #aaa;
        }
        .btn-add {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            padding: 8px 16px;
            font-size: 14px;
            font-weight: 500;
            color: white;
            background: #378add;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            text-decoration: none;
            transition: background 0.15s;
        }
        .btn-add:hover { background: #185fa5; color: white; }
        .btn-add svg {
            width: 14px;
            height: 14px;
            stroke: white;
            fill: none;
            stroke-width: 2.5;
            stroke-linecap: round;
        }
    </style>
</head>
<body>

<div class="todo-card">

    <!-- Header -->
    <div class="todo-header">
        <div class="todo-header-left">
            <div class="todo-icon">
                <svg viewBox="0 0 24 24">
                    <path d="M9 11l3 3L22 4"/>
                    <path d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11"/>
                </svg>
            </div>
            <div>
                <p class="todo-title">My todos</p>
                <p class="todo-subtitle">Manage and track your tasks</p>
            </div>
        </div>
        <span class="welcome-badge">Welcome, <strong>${name}</strong></span>
    </div>

    <!-- Table -->
    <div class="todo-body">
        <c:choose>
            <c:when test="${empty todos}">
                <div class="empty-state">
                    <svg viewBox="0 0 24 24">
                        <rect x="3" y="3" width="18" height="18" rx="2"/>
                        <path d="M9 12h6M12 9v6"/>
                    </svg>
                    <p>No todos yet. Add your first task!</p>
                </div>
            </c:when>
            <c:otherwise>
                <table class="todo-table">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Description</th>
                        <th>User</th>
                        <th>Target date</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${todos}" var="todo">
                        <tr>
                            <td class="td-id">${todo.id}</td>
                            <td class="td-desc">${todo.description}</td>
                            <td class="td-user">${todo.username}</td>
                            <td class="td-date">${todo.targetDate}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${todo.done}">
                                            <span class="badge-done yes">
                                                <span class="badge-dot"></span> Done
                                            </span>
                                    </c:when>
                                    <c:otherwise>
                                            <span class="badge-done no">
                                                <span class="badge-dot"></span> Pending
                                            </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div style="display:flex; gap:6px;">
                                    <a href="update-todo?id=${todo.id}" class="btn-action btn-edit">
                                        <svg viewBox="0 0 24 24">
                                            <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                                            <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
                                        </svg>
                                        Edit
                                    </a>
                                    <a href="delete-todo?id=${todo.id}" class="btn-action btn-delete">
                                        <svg viewBox="0 0 24 24">
                                            <polyline points="3 6 5 6 21 6"/>
                                            <path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"/>
                                            <path d="M10 11v6M14 11v6"/>
                                            <path d="M9 6V4h6v2"/>
                                        </svg>
                                        Delete
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Footer -->
    <div class="todo-footer">
        <span class="todo-count">
            <c:choose>
                <c:when test="${empty todos}">No tasks</c:when>
                <c:otherwise>${fn:length(todos)} task(s)</c:otherwise>
            </c:choose>
        </span>
        <a href="add-todo" class="btn-add">
            <svg viewBox="0 0 24 24">
                <line x1="12" y1="5" x2="12" y2="19"/>
                <line x1="5" y1="12" x2="19" y2="12"/>
            </svg>
            Add todo
        </a>
    </div>

</div>

<script src="webjars/jquery/3.6.0/jquery.min.js"></script>
<script src="webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js"></script>

</body>
</html>