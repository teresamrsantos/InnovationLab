
    String.prototype.stripSlashes = function(){
        return this.replace(/\\(.)/mg, "$1");
    }

    function escapeHtml(text) {
        var map = {
          '&': '&amp;',
          '<': '&lt;',
          '>': '&gt;',
          '"': '&quot;',
          "'": '&#039;'
        };
        
        return text.replace(/[&<>"']/g, function(m) { return map[m]; });
      }
    export function dataready(data) {
        //https://stackoverflow.com/questions/5326165/use-javascript-to-stripslashes-possible
        data.trim() 
        data.stripSlashes()
        data = escapeHtml(data);
        return data;
        } 