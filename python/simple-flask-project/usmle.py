from flask import Flask
from flask import render_template
from flask_wtf import Form
from wtforms import TextField
from wtforms import StringField, BooleanField
from wtforms.validators import DataRequired
import random

class LoginForm(Form):
    #,  validators=[DataRequired()]
    question = StringField('question')
    answer = StringField('answer')
    prevanswer = StringField('prevanswer')


class ValueObject:
    def __init__(self,question,answer,panswer):
        self.question = question
        self.answer =  answer
        self.prevanswer = panswer


class QA:
    def __init__(self, file_name):
        self.data = []
        f = open (file_name,'r')
        i=0;
        for line in f:
            self.data.insert(i,line)
            i=i+1

app = Flask(__name__)
app.config.from_object('config')
app.QA = QA("templates/qa.txt")
app.QAlen = len(app.QA.data)
rnd = random.randint(0,app.QAlen-1)
line = app.QA.data[rnd]
tokens=line.split(",")

app.vo = ValueObject(tokens[0],tokens[1],"NA");


@app.route('/', methods=['GET', 'POST'])
@app.route('/usmle/<name>')

def login(name='Umarani Venkatesh'):
    form = LoginForm()
    
    if form.validate_on_submit():
        #flash('you answered correctly!, answer next question now')
        rnd = random.randint(0,app.QAlen-1)
        line = app.QA.data[rnd]
        tokens=line.split(",")
        app.vo = ValueObject(tokens[0],tokens[1],app.vo.question + " -> " + app.vo.answer);
    
        
    return render_template('usmle.html',
                               title='Sign In',
                               form=form,name=name,vo=app.vo)
if __name__ == "__main__":
    app.run()


    
