package com.example.todo.controller.task;

import com.example.todo.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor//requiredされている引数があるコンストラクタを初期化する
@RequestMapping("/tasks")//GetMappingで共通してtasksにアクセスしてくれるからその後のみpathのみでいい
public class TaskController {

    private final TaskService taskService;//finalは内容が変更されることのない様にするもの（変数とかに使える）

    //コンストラクター（ここでDIのために初期化）←これがないとTaskServiceがnullになる
    // DIとは使いたいオブジェクトを直接newせずに外部から渡してもらう

    //レクチャー33付近はマジでわけわかめ
    @GetMapping // GET /tasksを受け取るハンドラーメソッド
    public String list(TaskSeacrhForm seacrhForm, Model model){ //List<TaskEntity> -> List<TaskDTO>
        var taskList = taskService.find(seacrhForm.toEntity())
               .stream()
               .map(TaskDTO::toDTO)
               .toList();
        model.addAttribute("taskList", taskList);
        model.addAttribute("searchDTO", seacrhForm.toDTO());
        return "tasks/list";
        //メモ：テンプレートを指定するときは拡張子を記載する必要はない
    }
    @GetMapping("/{id}")
    public String showService(@PathVariable("id") long taskId, Model model) {
        var taskDTO = taskService.findById(taskId)
                .map(TaskDTO::toDTO)
                .orElseThrow(TaskNotFoundException::new);
        //戻り値の方を知りたいときはCtrl+Shift+p
        model.addAttribute("task", taskDTO);//req92むずい
        return "tasks/detail";
    }

    //GET  /tasks/creationForm
    @GetMapping("/creationForm")
    public String showCreationForm(@ModelAttribute TaskForm form, Model model) {
        model.addAttribute("mode", "CREATE");
        //@ModelAttribute　←これでattributeと同じ効力+勝手に初期化 model.addAttribute("taskForm", form);
        return "tasks/form";
    }

    //POST  /tasks
    @PostMapping
    public String create(@Validated TaskForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return showCreationForm(form, model);//バリデーションエラーの時に入力データが消えないように値を渡す
        }
        taskService.create(form.toEntity());
        return "redirect:/tasks";
        //二重サブミット対策：直接アクセスせず(300でアクセス先を指定)、redirect(200)することでリロード時に同じ処理を繰り返さずに済む
    }

    //GET /tasks/{taskId}/editForm
    @GetMapping("/{id}/editForm")
    public String showEditForm(@PathVariable("id") Long id, Model model){//@PathVariableでidを取ってきてLong型のidに入れる
        var form = taskService.findById(id)
                .map(TaskForm::fromEntity)
                        .orElseThrow(TaskNotFoundException::new);
        model.addAttribute("mode", "EDIT");
        //optionalを剥がすためにorElseThrowをつけてる。値ない時はTaskNotFoundに投げる
        model.addAttribute("taskForm", form);//taskFormというキーでformをtemplateに渡せる
        return "tasks/form";
    }

    @PutMapping("{id}") // PUT /tasks/{id}
    public String update(@PathVariable("id") long id,
                         @Validated @ModelAttribute TaskForm form,
                         BindingResult bindingResult,
                         Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("model", "EDIT");
            return "tasks/form";
        }
        var entity = form.toEntity(id);
        taskService.update(entity);
        return "redirect:/tasks/{id}";
    }

    // POST /tasks/1 (hidden: _method: delete)
    // -> DELETE /tasks/1　spring内でに読み替えてくれる　apprication.properiesであれをtrueにしてるから
    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") long id) {
        taskService.delete(id);
        return "redirect:/tasks";
    }
}
