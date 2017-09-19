<div class="content-wrapper">
    <section class="content-header">
        <h1>
        ${title}
            <small>${summary}</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/"><i class="fa fa-dashboard"></i> 首页 </a></li>
            <li class="active">${title}</li>
        </ol>
    </section>

    <section class="content">
    <#if page??><#include "${page}"></#if>
    </section>
</div>