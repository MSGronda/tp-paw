DROP VIEW IF EXISTS subjectreviewstatistics;

CREATE VIEW subjectreviewstatistics AS
SELECT
    idsub,
    reviewcount,
    easycount,
    mediumcount,
    hardcount,
    nottimedemandingcount,
    timedemandingcount,
    averagetimedemandingcount,

    case
        when easycount = 0 and mediumcount = 0 and hardcount = 0 then null
        when easycount > mediumcount and easycount > hardcount then 0
        when mediumcount > easycount and mediumcount > hardcount then 1
        when hardcount > easycount and hardcount > mediumcount then 2
        else 1
        end as difficulty,

    case
        when nottimedemandingcount = 0 and timedemandingcount = 0 and averagetimedemandingcount = 0 then null
        when nottimedemandingcount > timedemandingcount and nottimedemandingcount > averagetimedemandingcount then 0
        when averagetimedemandingcount > nottimedemandingcount and averagetimedemandingcount > timedemandingcount then 1
        when timedemandingcount > nottimedemandingcount and timedemandingcount > averagetimedemandingcount then 2
        else 1
        end as timedemanding

FROM (
         SELECT
             idsub,
             count(*)                                        reviewcount,
             count(case easy when 0 then 1 end)              easycount,
             count(case easy when 1 then 1 end)              mediumcount,
             count(case easy when 2 then 1 end)              hardcount,
             count(case timedemanding when 0 then 1 end)     nottimedemandingcount,
             count(case timedemanding when 2 then 1 end)     timedemandingcount,
             count(case timedemanding when 1 then 1 end)     averagetimedemandingcount
         FROM reviews
         GROUP BY idsub
) s1;
