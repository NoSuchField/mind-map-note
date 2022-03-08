CREATE TABLE `t_question` (
    `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `uuid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `title` text COLLATE utf8mb4_unicode_ci,
    `detail` text COLLATE utf8mb4_unicode_ci,
    `comment` text COLLATE utf8mb4_unicode_ci,
    `rank` bigint(20) unsigned NOT NULL DEFAULT '1',
    `parent` int(10) unsigned DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;