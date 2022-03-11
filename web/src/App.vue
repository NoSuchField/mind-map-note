<template>
  <div id="app" style="width: 100%" :class="{ 'app-no-padding': read, 'app-padding': !read }">

  <div class="header" style="position: fixed; top: 0" v-if="!read">
        

        <div class="header-edit-btn" v-if="currentQuestion.id" @click="overview">Overview</div>

        <div class="header-edit-btn" v-if="currentQuestion.id" @click="setMoveTarget">Move Target</div>

        <div class="header-edit-btn" v-if="moveTarget !== 0" @click="moveQuestion">Move</div>

        <div class="header-edit-btn" v-if="currentQuestion.id" @click="setAsRoot">Set Root</div>

        <div class="header-edit-btn" v-if="currentQuestion.id" @click="addRoot">Add Root</div>

        <div class="header-edit-btn" v-if="currentQuestion.id" @click="newQuestion">Add Child</div>
        <div class="header-edit-btn" v-if="!currentQuestion.id" @click="cancelNew">Cancel</div>

        <div class="header-edit-btn" @click="toggleEdit">Save</div>

        <div class="header-edit-btn" v-if="currentQuestion.id" @click="upDownQuestion('up')">Up</div>
        <div class="header-edit-btn" v-if="currentQuestion.id" @click="upDownQuestion('down')">Down</div>
        

        <div id="btnSave" class="header-edit-btn" @click="hideEdit">Hide Edit</div>

        <div class="header-edit-btn" v-if="currentQuestion.id" @click="deleteQuestion">Delete</div>
      </div>

  <div class="left-pan" v-if="!read">

  <VueBlocksTree :props="{label: 'label', expand: 'expand', children: 'children',  key:'id'}" :data="treeData" :horizontal="true" :collapsable="true" @node-click="onNodeClick"></VueBlocksTree>
  
  </div>

  <div style="display: flex; flex-direction: column" :class="{ 'right-pan': read, 'right-pan-no-editor': !read }">

  <input type="text" class="title-input" v-model="currentQuestion.title" />

    <div>
      <div class="markdown-body" v-html="questionHtml"></div>
    </div>


    

    <!--
    <div class="markdown-body" v-html="answerHtml"></div>

    <div class="cm">
      <Codemirror
        v-model:value="currentQuestion.comment"
        :options="cmOptions"
        border
        placeholder="test placeholder"
        :height="900"
        @change="renderAnswer"
      />
    </div>

    -->

  </div>

  <div class="cm" id="codeMirrorEditor" v-if="showEditor">
      <Codemirror
        v-model:value="currentQuestion.detail"
        :options="cmOptions"
        border
        placeholder="test placeholder"
        :height="1000"
        @change="renderQuestion"
      />
    </div>

  </div>
</template>

<script>

import hljs from 'highlight.js';
import 'highlight.js/styles/github.css';

import Codemirror from "codemirror-editor-vue3";

import java from 'highlight.js/lib/languages/java';
hljs.registerLanguage('java', java);

import { ref,reactive } from 'vue';

import VueBlocksTree from "vue3-blocks-tree";
import "vue3-blocks-tree/dist/vue3-blocks-tree.css";

import baseUrl from '@/config/env'

export default {
  name: 'App',
  components: {
    Codemirror,
    VueBlocksTree
  },
  setup() {

    let selected = ref([]);
    let treeOrientation = ref("0");

    const toggleSelect = (node, isSelected) => {
        isSelected ? selected.value.push(node.some_id) : selected.value.splice(selected.value.indexOf(node.some_id), 1);
        if(node.children && node.children.length) {
            node.children.forEach(ch=>{
                toggleSelect(ch,isSelected)
            })
        }
    }

     const fetchAsync = () => {
      const xhr = new XMLHttpRequest();

    let params = new URLSearchParams('?' + window.location.href.split('?')[1])
    
    let id = params.get('id')
    //this.read = params.get('read')

    //if (this.read && id) {
        // this.getNode(id)
    //}

    if (id) {   
        localStorage.setItem("rootId", id)
    }

      let rootId = localStorage.getItem("rootId")
      let flag = rootId && rootId != 0
      xhr.open('GET', baseUrl + '/question/tree' + (flag ? ('?rootId='+rootId) : ''), false);
      xhr.setRequestHeader('Content-Type', 'application/json');

      
      xhr.send()
      const response = JSON.parse(xhr.responseText);
      return response.result;
    }

    let treeNodes = fetchAsync();

    let data = {
      cmOptions: {
        mode: "markdown",
        lineNumbers: true, // Show line number
        smartIndent: true, // Smart indent
        indentUnit: 4, // The smart indent unit is 2 spaces in length
        foldGutter: true, // Code folding
        styleActiveLine: true, // Display the style of the selected row
      },
      treeData: reactive(treeNodes),
      selected,
      toggleSelect,
      treeOrientation
    }

    return data
  },

  mounted() {
    var MarkdownIt = require('markdown-it')
    this.md = new MarkdownIt()
    this.he = require('he');
    
    let rootId = localStorage.getItem("rootId")
    
    if (!(rootId && rootId != 0)) {
      document.getElementsByClassName("org-tree-node-label")[0].style.display="none"
    }

    let params = new URLSearchParams('?' + window.location.href.split('?')[1])
    
    let id = params.get('id')
    this.read = params.get('read')

    if (this.read && id) {
        this.getNode(id)
    }
  },
  methods: {

    moveQuestion: function() {
      if (this.moveTarget && this.moveTarget != 0) {
        this.currentQuestion.parent = this.moveTarget;
        
fetch(baseUrl + "/question/update", {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(this.currentQuestion),
        })
        .then(response => response.json())
        .then(data => {
          console.log('Success:', data);
          window.location.reload();
        })
        .catch((error) => {
          console.error('Error:', error);
        });
      }
    },

    overview: function() {
      localStorage.setItem("rootId", 0)
      window.location.href = "/";
    },

    setMoveTarget: function() {
      this.moveTarget = this.currentQuestion.id
      if(!this.moveTarget){
        this.moveTarget = 0
      }
    },
    statusAll: function(nodes, status) {
      for (let item of nodes) {
        item.expanded = status
        if (item.nodes && item.nodes.length) this.statusAll(item.nodes, status)
      }
    },

    expandAll: function(nodes) {
      this.statusAll(nodes, !this.allExpanded)
      this.allExpanded = !this.allExpanded
    },

    cancelNew: function() {
      this.currentQuestion = this.preQuestion

      this.renderQuestion()
      this.renderAnswer()
    },

    addRoot: function() {
      this.currentQuestion.id = undefined
      this.newQuestion()
    },

    newQuestion: function() {

      this.preQuestion = this.currentQuestion

      this.currentQuestion = {
        title: '',
        detail: '',
        comment: '',
        parent: this.preQuestion.id
      }

      this.renderQuestion()
      this.renderAnswer()
      this.justToggleEdit()
    },

    setAsRoot: function() {
      this.rootId = this.currentQuestion.id
      localStorage.setItem('rootId', this.rootId)
      window.location.href = "/?id=" + this.rootId;
    },

    getRandomQuestion: function() {
      fetch(baseUrl + '/question/random')
      .then(response => response.json())
      .then(data => {

        this.currentQuestion = data.result
        this.question = data.result.title
        this.answer = data.result.detail
        this.renderQuestion()
        this.renderAnswer()

      });
    },

    deleteQuestion: function() {
      let url = baseUrl + "/question/delete?id=" + this.currentQuestion.id
      fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({}),
        })
        .then(response => response.json())
        .then(data => {
          console.log('Success:', data);
          window.location.reload();
        })
        .catch((error) => {
          console.error('Error:', error);
        });
    },

    renderQuestion: function() {
      this.questionHtml = this.extractCode(this.md.render(this.currentQuestion.detail))
      
    },
    renderAnswer: function() {
      
      // this.answerHtml = this.extractCode(this.md.render(this.currentQuestion.comment))
    },
    justToggleEdit: function(){
    },

    upDownQuestion: function(str) {
      let url = baseUrl + "/question/" + str + "?id=" + this.currentQuestion.id
      fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({}),
        })
        .then(response => response.json())
        .then(data => {
          console.log('Success:', data);
        })
        .catch((error) => {
          console.error('Error:', error);
        });
    },
    toggleEdit: function(){

      let url = baseUrl + "/question/" + (this.currentQuestion.id ? "update" : "add")

      if (this.editMode) {

        fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(this.currentQuestion),
        })
        .then(response => response.json())
        .then(data => {
          console.log('Success:', data);
          window.location.reload();
        })
        .catch((error) => {
          console.error('Error:', error);
        });
      }

      let btnSave = document.getElementById("btnSave")
      let btnEdit = document.getElementById("btnEdit")

      

      btnEdit.style.display = this.editMode ? 'none' : ''

      btnSave.style.display = this.editMode ? '' : 'none'

      let editMode = this.editMode

      this.btnText = this.editMode ? 'Save' : 'Edit'

      let elements = document.getElementsByClassName("codemirror-container");

      Array.prototype.forEach.call(elements, function (element) {
        
        element.style.height=editMode ? '800px' : '0'
        element.style.border=editMode ? '1px solid #dddddd' : 'none'
        
      });
    },

    hideEdit: function(){
      // let elements = document.getElementsByClassName("codemirror-container");
      // Array.prototype.forEach.call(elements, function (element) {
      //   element.style.height='0'
      //   element.style.border='none'
      // });

      this.showEditor = !this.showEditor
    },

    travelNodes: function(nodes) {
      for (let item of nodes) {

        item.isCurrent = false
       
        if (item.nodes && item.nodes.length) this.travelNodes(item.nodes)
      }
    },

    onNodeClick (e, node) {

      if (this.currentNode.nodeName) {
        this.currentNode.style.backgroundColor="#FFFFFF"
        this.currentNode.style.color="#2F3F52"
      }

      if (e.target.nodeName == "SPAN") {
        e.target.parentElement.style.backgroundColor="#0BBC7A"
        e.target.parentElement.style.color="#FFFFFF"
        this.currentNode = e.target.parentElement
      } else {
        e.target.style.backgroundColor="#0BBC7A"
        e.target.style.color="#FFFFFF"
        this.currentNode = e.target
      }

      
        this.getNode(node.id)
    },

    getNode: function(id) {
        let url = baseUrl + "/question/get?id=" + id
        fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
            })
            .then(response => response.json())
            .then(data => {
            this.currentQuestion = data.result

            this.renderQuestion()
            this.renderAnswer()

            })
            .catch((error) => {
            console.error('Error:', error);
            });
    },
    
    extractCode: function(html) {
     
     let dec = this.he.decode(html);
      let result = ''

      let arr = dec.split(/<\/*pre>/)

        arr.forEach(function(item){
          let sub = item

          if (item.startsWith('<code')) {

            let gg = item.split(/<code class="language-(.*)">/)

            let lang = gg[1]

            let content = gg[2].substring(0, gg[2].length - 7)

            sub = hljs.highlight(content, {language: lang, ignoreIllegals: true}).value
            sub = '<pre><code>' + sub + '</code></pre>'
          }
          result = result + sub
        })

        return result
      }

    
  },
  data() {
    return {
      read: false,
      showEditor: false,
      rootId: 0,
      moveTarget: 0,
      allExpanded: false,
      currentNode: {},
      preQuestion: {},
      newMode: false,
      currentQuestion: {},
      editMode: true,
      btnText: 'Edit',
      he: {},
      md: {},
      hljs: {},
      question: '',
      answer: '',
      questionHtml: '',
      answerHtml: '',
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  display: flex;
  flex-direction: row;
}

* {
  margin: 0;
  padding: 0;
}

html, body {
  height: 100%;
}

.markdown-body {
  min-height: 100%;
  padding: 20px;
}

.CodeMirror {
  font-size: 20px;
  font-family: monospace !important;
}

.codemirror-container {
  height: 0;
  transition: height 0.3s;
  border: none;
}

.CodeMirror-line, .CodeMirror-linenumber {
  font-family: monospace !important;
}

.header {
  height: 60px;
  background-color: #F4F4F4;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
}

.header-left {
  flex-grow: 1
}

.header-edit-btn {
  width: 120px;
  background-color: #52CAFF;
  height: 40px;
  line-height: 40px;
  text-align: center;
  font-size: 20px;
  user-select: none;
  cursor: pointer;
  margin: 10px;
  border-radius: 5px;
}

.header-edit-btn-alt {
  width: 110px;
  background-color: #F4F4F4;
  height: 70px;
  line-height: 70px;
  text-align: center;
  font-size: 20px;
  user-select: none;
  cursor: pointer;
  
  width: 100%;
  margin-top: -7px;
  color: #000000;

}

.cm {
  width: 920px;
  position: fixed;
  left: 0;
}

.header {
	width: 100%;
	
}

.left-pan {
	flex-basis: 920px;
	overflow-y: scroll;
	position: fixed;
	left: 0;
	width: 920px;
	height: 100%;
  background-color: #FDFDFD;
}

.right-pan {
	flex-grow: 1;
}

.right-pan-no-editor {
  flex-grow: 1;
	padding-left: 920px;
}

.title-input{
	padding: 10px;
	border-radius: 5px;
	margin: 20px;
	border: 1px dashed #666666;
  outline: none;
}

.header-edit-btn{
	width: 100px;
	font-size: 15px;
	background-color: #FFFFFF;
	border: 1px solid rgba(0, 0, 0, 0.2);

}

.org-tree-node-label .org-tree-node-label-inner {
	user-select: none;
	min-width: 100px;
	white-space: nowrap;
	border-radius: 5px;
	background-color: #FFFFFF;
}

.org-tree-container {
	background-color: #FDFDFD;
  margin-bottom: 150px;
}

.CodeMirror {
  border: 1px solid #eee;
  height: calc(100%-60px);
}

.CodeMirror-scroll {
  height: calc(100%-60px);
  overflow-y: hidden;
  overflow-x: auto;
}

#codeMirrorEditor {
  height: calc(100%-60px);
}

.app-padding {
    padding-top: 30px;
}

.app-no-padding {
    padding-top: 0;
}
</style>
