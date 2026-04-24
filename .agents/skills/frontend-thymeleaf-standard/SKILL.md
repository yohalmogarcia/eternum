---
name: frontend-thymeleaf-standard
description: 'Organizational standards for Frontend development using Thymeleaf, Tailwind CSS, and JavaScript.'
---

# Frontend Development Standards (Thymeleaf & Client Logic)

Your goal is to assist in the creation of high-quality frontend interfaces by adhering to the specific architectural and stylistic standards defined for Thymeleaf-based projects.

## 1. Tech Stack
- **Template Engine:** Thymeleaf (Server-side rendering).
- **Styling Frameworks:** Bootstrap (Interface design) and Tailwind CSS (Utility-First approach).
- **JS Libraries:** jQuery / Vanilla JS.
- **Components:** DataTables for data table management.

## 2. File Structure & Organization
- **Templates (`/src/main/resources/templates/`):**
    - `layout/`: Master base templates.
    - `fragments/`: Reusable components (headers, footers, buttons, modals).
    - `pages/`: Module-specific views (e.g., `/pages/users/`).
- **Static Resources (`/src/main/resources/static/`):**
    - `js/`: Mandatory location at `static/js/[module]/Controller.js`.
    - `css/`: Tailwind source files and global styles.
    - `img/`: Optimized resources in SVG or WebP format.

### Basic Syntax
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title th:text="${title}">Default</title>
</head>
<body>
    <h1 th:text="${message}">Hello</h1>
    <p th:text="'Welcome, ' + ${name}">Welcome, User</p>
</body>
</html>
```

### Variables & Loops
```html
<!-- Variable -->
<p th:text="${user.name}">Name</p>

<!-- Loop -->
<tr th:each="user : ${users}">
    <td th:text="${user.name}">Name</td>
    <td th:text="${user.email}">Email</td>
</tr>

<!-- Conditionals -->
<p th:if="${user.active}">Active</p>
<p th:unless="${user.active}">Inactive</p>
```

## 3. HTML & Thymeleaf Best Practices
- **Semantics:** Mandatory use of HTML5 tags (`<header>`, `<main>`, `<section>`, `<footer>`, `<nav>`).
- **Fragmentation:** Fragment any element repeated in more than two views.
    - Use `th:replace` for layouts.
    - Use `th:insert` for minor components.
- **View Logic:** Keep templates simple. Avoid complex logic inside `th:if` or `th:each`.
- **Route Management:** Strict use of `@{/path}` for static resources and links to ensure correct context resolution.

## 4. Modular CSS with Tailwind
- **Class Usage:** Prefer direct utilities in HTML over excessive use of `@apply`.
- **Consistency:** Use only the Tailwind spacing scale (p-4, m-2, etc.). Avoid arbitrary values.
- **Configuration:** Corporate colors and fonts must reside in `tailwind.config.js`.
- **Class Ordering:** 1. Layout, 2. Box Model, 3. Typography, 4. Visuals, 5. Interactivity.

## 5. JavaScript & Client Logic
- **Syntax:** Mandatory ES6+ (const/let, arrow functions, destructuring, template literals).
- **DOM Manipulation:** Use data selectors (`data-js="element"`) instead of CSS classes for logic.
- **Modularization:** Load scripts as modules (`type="module"`) and decouple them by page or component.
- **Events:** Mandatory cleanup of dynamically created listeners to prevent memory leaks.

## 6. UI, UX & Accessibility
- **Responsiveness:** Mobile-First design using Tailwind prefixes (`md:`, `lg:`, etc.).
- **Visual Feedback:** Mandatory use of spinners or skeletons during asynchronous processes.
- **Accessibility (a11y):** - Minimum contrast of 4.5:1.
    - Mandatory `aria-label` and `alt` attributes.
    - Functional keyboard navigation.

## 7. Naming Conventions
- **HTML/Thymeleaf Files:** `kebab-case.html` (e.g., `user-detail.html`).
- **JS Files:** `PascalCase` for classes; `camelCase` for functions and modules.
- **Thymeleaf Variables:** `camelCase` to maintain consistency with Java objects.

## 8. Communication Protocol
- All JSON responses handled via JavaScript must expect the standard `ResponseHandler` structure:
  ```json
  {
    "code": [int],
    "message": [string],
    "status": [HttpStatus],
    "data": [Object/List]
  }
  ```