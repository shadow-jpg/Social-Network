
    <a class="btn btn-primary my-3" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
        Add Posts
    </a>
    <div class="collapse" id="collapseExample">
        <div class="form-group mt-3 my-3">
            <form method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <input type="hidden" name="_csrf"  value="${_csrf.token}" />
                </div>

                <div class="form-group">
                    <input type="text" name="text" class="form-control" placeholder="Message"/>
                </div>

                <div class="form-group">
                    <input type="text" name="tag" class="form-control" placeholder="tag" />
                </div>

                <div class="form-group">
                    <div class="custom-file">
                        <input type="file" name="file" id="customFile">
                        <label class="custom-file-label" for="customFile">Pick file</label>
                    </div>
                </div>

                <input type="hidden" name="id" value="<#if message??>${message.id}</#if>"/>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary "> Save posts </button>
                </div>
            </form>
        </div>
    </div>