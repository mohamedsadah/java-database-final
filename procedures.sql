USE inventory;

DELIMITER $$

DROP PROCEDURE IF EXISTS GetMonthlySalesForEachStore$$
CREATE PROCEDURE GetMonthlySalesForEachStore(
    IN year_param INT,
    IN month_param INT
)
BEGIN
    SELECT
        od.store_id,
        SUM(DISTINCT od.total_price) AS total_sales,
        MONTH(od.date) AS sale_month,
        YEAR(od.date) AS sale_year
    FROM order_details od
    WHERE YEAR(od.date) = year_param
      AND MONTH(od.date) = month_param
    GROUP BY od.store_id, MONTH(od.date), YEAR(od.date)
    ORDER BY total_sales DESC;
END$$

DROP PROCEDURE IF EXISTS GetAggregateSalesForCompany$$
CREATE PROCEDURE GetAggregateSalesForCompany(
    IN year_param INT,
    IN month_param INT
)
BEGIN
    SELECT
        SUM(DISTINCT od.total_price) AS total_sales,
        MONTH(od.date) AS sale_month,
        YEAR(od.date) AS sale_year
    FROM order_details od
    WHERE YEAR(od.date) = year_param
      AND MONTH(od.date) = month_param
    GROUP BY MONTH(od.date), YEAR(od.date);
END$$

DROP PROCEDURE IF EXISTS GetTopSellingProductsByCategory$$
CREATE PROCEDURE GetTopSellingProductsByCategory(
    IN target_month INT,
    IN target_year INT
)
BEGIN
    SELECT
        category_sales.category,
        category_sales.name,
        category_sales.total_quantity_sold,
        category_sales.total_sales
    FROM (
        SELECT
            p.category,
            p.name,
            SUM(oi.quantity) AS total_quantity_sold,
            SUM(oi.quantity * p.price) AS total_sales
        FROM order_item oi
        JOIN order_details od ON oi.order_id = od.id
        JOIN product p ON oi.product_id = p.id
        WHERE MONTH(od.date) = target_month
          AND YEAR(od.date) = target_year
        GROUP BY p.category, p.name
    ) AS category_sales
    WHERE category_sales.total_quantity_sold = (
        SELECT
            MAX(sub.total_quantity_sold)
        FROM (
            SELECT
                p2.category,
                SUM(oi2.quantity) AS total_quantity_sold
            FROM order_item oi2
            JOIN order_details od2 ON oi2.order_id = od2.id
            JOIN product p2 ON oi2.product_id = p2.id
            WHERE MONTH(od2.date) = target_month
              AND YEAR(od2.date) = target_year
              AND p2.category = category_sales.category
            GROUP BY p2.name
        ) AS sub
    )
    ORDER BY category_sales.category, category_sales.total_quantity_sold DESC;
END$$

DROP PROCEDURE IF EXISTS GetTopSellingProductByStore$$
CREATE PROCEDURE GetTopSellingProductByStore(
    IN target_month INT,
    IN target_year INT
)
BEGIN
    SELECT
        store_sales.product_name,
        store_sales.store_id,
        store_sales.total_quantity_sold,
        store_sales.total_sales
    FROM (
        SELECT
            s.id AS store_id,
            p.name AS product_name,
            SUM(oi.quantity) AS total_quantity_sold,
            SUM(oi.quantity * p.price) AS total_sales
        FROM order_item oi
        JOIN order_details od ON oi.order_id = od.id
        JOIN product p ON oi.product_id = p.id
        JOIN store s ON od.store_id = s.id
        WHERE MONTH(od.date) = target_month
          AND YEAR(od.date) = target_year
        GROUP BY s.id, p.name
    ) AS store_sales
    WHERE store_sales.total_quantity_sold = (
        SELECT
            MAX(sub.total_quantity_sold)
        FROM (
            SELECT
                p2.name,
                SUM(oi2.quantity) AS total_quantity_sold,
                od2.store_id
            FROM order_item oi2
            JOIN order_details od2 ON oi2.order_id = od2.id
            JOIN product p2 ON oi2.product_id = p2.id
            WHERE MONTH(od2.date) = target_month
              AND YEAR(od2.date) = target_year
              AND od2.store_id = store_sales.store_id
            GROUP BY p2.name
        ) AS sub
    )
    ORDER BY store_sales.store_id, store_sales.total_quantity_sold DESC;
END$$

DELIMITER ;
