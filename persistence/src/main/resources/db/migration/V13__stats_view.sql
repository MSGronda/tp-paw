DROP TABLE IF EXISTS subjectreviewstatistics;

CREATE VIEW subjectreviewstatistics AS
SELECT
    idsub,
    count(*)                                        reviewcount,
    count(case easy when 0 then 1 end)              easycount,
    count(case easy when 1 then 1 end)              mediumcount,
    count(case easy when 2 then 1 end)              hardcount,
    count(case timedemanding when 0 then 1 end)     nottimedemandingcount,
    count(case timedemanding when 2 then 1 end)     timedemandingcount,
    count(case timedemanding when 1 then 1 end)     averagetimedemandingcount
FROM paw.public.reviews
GROUP BY idsub;

CREATE INDEX idx_reviews_idsub ON reviews(idsub);
